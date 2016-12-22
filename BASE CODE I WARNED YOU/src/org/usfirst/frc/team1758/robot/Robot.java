package org.usfirst.frc.team1758.robot;

//import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import java.io.IOException;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import java.io.InputStream;
//import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CameraServer;
import org.json.*;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import java.net.*;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must lso update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
	
	
	RobotDrive tmDrive;
	Joystick xBox1;
	Joystick xBox2;
	ADXRS450_Gyro aGyro;
	CameraServer mCam;
	AxisCamera technoCam;
	Image frame;
	Relay mCamLight;
	Ultrasonic mRange1;
	Encoder mArmPos1;
	Timer autoTimer1;
	Timer autoTimer2;
	Talon mShooter_1;
	Talon mShooter_2;
	Relay mAdjustAngle;
	Talon mMoveBall;
	Talon mPickUpBall;
	AnalogPotentiometer armAngleCalculator;
	Encoder distanceDriven;
	Talon mWheelOnTheBack;
	double m_distanceDriven = 0.0;
    double aGyroAngle = 0.0;
	double mLeftDistance = 0.0;
	double mRightDistance = 0.0;
	double m_RangeInches1 = 0.0;
	double m_AutoTimer1 = 0.0;
	double m_AutoTimer2 = 0.0;
	double m_xBox1X = 0.0;
	double m_xBox1Y = 0.0;
	double m_xBox2X = 0.0;
	double m_xBox2Y = 0.0;
	int thrust = 0;
	double m_armAngleCalculator = 0.0;
	//double mdistance =  0.0;
	int m_AutoState = 0;
	//int currSession;
	//int sessionfront;
	//int sessionback;
	final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     * @return 
     */
    
    private static String readAll(Reader rd) throws IOException
    {
    	StringBuilder sb = new StringBuilder();
    	int cp;
    	while((cp = rd.read()) != -1)
    	{
    		sb.append((char) cp);
    	}
    return sb.toString();
    }
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException
    {
    	HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();
    	conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String jsonText = readAll(rd);
		JSONObject json = new JSONObject(jsonText);
		return json;
    }

    public void robotInit() {
    
    	//distanceDriven = new Encoder(0, 1, false);
    	tmDrive = new RobotDrive(0,1);
      	xBox1 = new Joystick(0);
    	xBox2 = new Joystick(1);
    	aGyro = new ADXRS450_Gyro();
  	//NetworkTable.setClientMode();
    //NetworkTable.setIPAddress("10.17.58.22");
    
	technoCam = new AxisCamera("10.17.58.11");
    	autoTimer1 = new Timer();
    	autoTimer2 = new Timer();
    	mShooter_1 = new Talon(5);
    	mShooter_2 = new Talon(4);    	
    	mPickUpBall = new Talon(2);
    	mMoveBall = new Talon (3);
    	//mAdjustAngle= new Relay(0);
    	mCamLight = new Relay(3);
    	mWheelOnTheBack = new Talon(6);
    	armAngleCalculator = new AnalogPotentiometer(0,360.0); 
    	chooser = new SendableChooser();
    	distanceDriven = new Encoder(8,9);
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        
        SmartDashboard.putData("Auto choices", chooser);
        //tmDrive.setInvertedMotor(MotorType.kFrontLeft,true);
        //tmDrive.setInvertedMotor(MotorType.kFrontRight,false);
        //frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        //sessionfront = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        //sessionback = NIVision.IMAQdxOpenCamera("cam2", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        //currSession = sessionfront;
        //NIVision.IMAQdxConfigureGrab(currSession);
        //Setup Gyro 
        //aGyro.calibrate();
        //aGyroAngle = (aGyro.getAngle());
        //SmartDashboard.putNumber("Gyro Angle", aGyroAngle);
        //setup camera server 	
        mCam = CameraServer.getInstance();
        mCam.setQuality(100);
        mCam.startAutomaticCapture("cam0");
        //Setup encoders
        
        distanceDriven.setDistancePerPulse((8.00 * 3.1415)/360);
        distanceDriven.reset();
        m_distanceDriven = distanceDriven.getDistance();
        SmartDashboard.putNumber("Distance Driven", m_distanceDriven);
        
        //mLeftDrive.setDistancePerPulse((8.00)/360);
        //mLeftDrive.reset();
        //mRightDrive.reset();
		//mLeftDistance = mLeftDrive.getDistance();
		//mRightDistance = mRightDrive.getDistance();
        //SmartDashboard.putNumber("Left Distance", mLeftDistance);
        //SmartDashboard.putNumber("Right Distance", mRightDistance);
        //setup range finder
        mRange1 = new Ultrasonic(2,1);
        mRange1.setAutomaticMode(true);
        m_RangeInches1 = mRange1.getRangeInches();
        SmartDashboard.putNumber("Distance", m_RangeInches1);
        
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void	 autonomousInit()
    {
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		tmDrive.setMaxOutput(.75);
		//SmartDashboard.putNumber("Distance Driven", distanceDriven.getDistance());
		m_AutoState++;
		
		
		m_AutoTimer1 = 3.0;
		autoTimer1.reset();
		autoTimer1.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    //RIP AUTONOMOUS Might go too far Captain's order...
    public void autonomousPeriodic() {
    	
    //Moat, Ramparts, Rock Wall, Rough Terrain
	  switch(m_AutoState)
		{
		case 1:
			if(autoTimer1.hasPeriodPassed(1.0))
			{
				m_AutoState = 2;
			}
    	case 2:
        //Put custom auto code here 
    		tmDrive.tankDrive(0.825,0.825);
    		if (autoTimer1.hasPeriodPassed(m_AutoTimer1))
    		{
    		m_AutoState = 3;
    		}else if(m_distanceDriven > 70.0)
    		{m_AutoState = 3;}
            break;
    	case 3:
    		tmDrive.tankDrive(0.0, 0.0);	
    		m_AutoState = 4;
    	  	break;
    	case 4:
    		autoTimer1.reset();
    		m_AutoState = 5;
    		break;
    	case 5:
    		m_AutoState = 6;
    		break;
    	case 6:
    		//shootBall();
    		break;
    	default:
    		break;
    	
    	}
	  /* Cheval De Frises
	  switch(m_AutoState)
	  {
	  case 1: 
		 m_AutoState = 2;
		 break;
	  case 2: 
		  autoTimer1.reset();
		  autoTimer1.start();
		  tmDrive.tankDrive(-.5, -.5);
		  if(autoTimer1.hasPeriodPassed(0.5))
		  {
			  m_AutoState = 3;
			  break; 
		  }
		  
	  case 3:
		  tmDrive.tankDrive(0.0, 0.0);
		   autoTimer1.reset();
		  autoTimer1.start();
		  m_AutoState = 4;
		 
		  break;
	  case 4:
		  
		  putDownBall();
		  if(autoTimer1.hasPeriodPassed(2.5))
		  {
			  m_AutoState = 5;
			  break;
		  }
	  case 5:
		  stopTouchingTheBall();
		  
		  autoTimer1.reset();
		  autoTimer1.start();
		  m_AutoState = 6;
		  break;
	  case 6:
		  
		  tmDrive.tankDrive(-.725, -.725);
		  if(autoTimer1.hasPeriodPassed(2.0))
		  {tmDrive.tankDrive(0.0, 0.0);
		  break;
		  }
	default:
		break;
		 }*/
	  /* Portcullis
	  switch(m_AutoState)
	  {
	  case 1: 
	
		  putDownBall();
		  if(autoTimer1.hasPeriodPassed(2.5))
		  {
			 
			  autoTimer1.reset();
		  	  autoTimer1.start(); 
		  	  m_AutoState = 2;
			  break;
		  }
	  case 2:
		  
		  stopTouchingTheBall();
		  autoTimer1.reset();
		  autoTimer1.start();
		  m_AutoState = 3;
		  break;
	  case 3:
		 
		  tmDrive.tankDrive(-.5,-.5);
		  if(autoTimer1.hasPeriodPassed(.6))
		  { 
			  
			  autoTimer1.reset();
		  	  autoTimer1.start();
		  	  m_AutoState = 4;
		      break;
		  }
	  case 4: 
		  tmDrive.tankDrive(0.0, 0.0);
		  
		  pickUpBall();
		  if(autoTimer1.hasPeriodPassed(1.5))
		  {
			  
			  autoTimer1.reset();
		  	  autoTimer1.start();
		  	  m_AutoState = 5;
		  	  break;
		  }
	  case 5:
		  stopTouchingTheBall();
		
		  tmDrive.tankDrive(-.725, -.725);
		  if(autoTimer1.hasPeriodPassed(3.0))
		  {
			  tmDrive.tankDrive(0.0, 0.0);
			  break;
		  }
	default:
		break;
		  }*/
	  }
	  
   
    public void teleopInit() {
    	//aGyro.reset();
    	//mLeftDrive.reset();
    	//mRightDrive.reset();
    	tmDrive.setMaxOutput(0.75);
    	Value value = Relay.Value.kForward;
    	mCamLight.set(value);
    	mShooter_1.set(0.0);
    	mShooter_2.set(0.0);
    
    }
   
    

    public void teleopPeriodic(){
    	//float value = (float) 1.0;
    	
    	//xBox1.setRumble(RumbleType.kLeftRumble, value);
    	//xBox1.setRumble(RumbleType.kRightRumble, value);
    	
        //aGyroAngle = (aGyro.getAngle());
        //SmartDashboard.putNumber("Gyro Angle", aGyroAngle);
    	//m_armAngleCalculator = (armAngleCalculator.get());
    	//SmartDashboard.putNumber("Arm Angle", m_armAngleCalculator);
    	
        SmartDashboard.putNumber("Range", m_RangeInches1);
        m_xBox1X = xBox1.getRawAxis(1)*(-1.0);
        m_xBox1Y = xBox1.getRawAxis(5)*(-1.0);
    	SmartDashboard.putNumber("Gyro angle", aGyro.getAngle());
    	
    	if(aGyro.getAngle() > 360.0 || aGyro.getAngle() < -360.0)
    	{ 
    		aGyro.reset();
    		//SmartDashboard.putNumber("Gyro angle", aGyro.getAngle());
    	}
    
        //These say what buttons do
      /*  if (xBox1.getRawButton(5)){
        	//if (currSession == sessionfront){
        		NIVision.IMAQdxStopAcquisition(currSession);
        		currSession = sessionback;
        		NIVision.IMAQdxConfigureGrab(currSession);
        	}
        	else if (currSession == sessionback){
        		NIVision.IMAQdxStopAcquisition(currSession);
        		currSession = sessionfront;
        		NIVision.IMAQdxConfigureGrab(currSession);
        	}
        }
       */
        //Change to 4
       
        if (xBox2.getRawButton(1))
        {
        	shootBall();
        }
        m_xBox2Y = xBox2.getRawAxis(2)*(-1.0);
        if (xBox2.getRawButton(6))
        {
        	stopBall();
        	//mCamLight.set(Relay.Value.kForward);
        }
        //GetRawAxis(5) is safety
        if (xBox2.getRawAxis(4) > 0.01)         
        {
        	moveBall();
        }
        else if(xBox2.getRawAxis(4) < -0.01 )
        {
        	moveBall();
        } 
        if (xBox2.getRawButton(3))
        		{pickUpBall();}
        else if(xBox2.getRawButton(5))
        		{ stopTouchingTheBall();}
        //This might work..
        if (xBox2.getRawButton(2))
        {
        	putDownBall();
        } 
        //else if (xBox2.getRawButton(2) == false)
        //{stopTouchingTheBall();}
        if(xBox2.getRawButton(4))
        {
        	mWheelOnTheBack.set(0.0);
        }
        if (xBox1.getRawButton(2))
        {
        	adjustAngle();
        }
        if (xBox1.getRawButton(3))
        {
    		adjustAngle1();
		}
        if (xBox1.getRawButton(4))
        {
        	stopAngle();
		}
        if (xBox2.getRawButton(8))
        {
        	wheelOnBack();
        }
        if (xBox2.getRawButton(7))
        {
        	wheelOnBackStop();
        }
      //  if (xBox1.getRawButton(1))
      //{
      //  	centerCalculate();
      //  }
        //These put the values for the input on the SmartDashboard
        SmartDashboard.putNumber("Xbox A",m_xBox1X);
        SmartDashboard.putNumber("Xbox B",m_xBox1Y);
        tmDrive.tankDrive(m_xBox1X, m_xBox1Y);
        SmartDashboard.putNumber("Distance", m_RangeInches1);
      
    }
    
    public void shootBall() 
    {
    	mShooter_1.set(-2.0);
    	mShooter_2.set(2.0);
    	//autoTimer1.reset();
    	//autoTimer1.start();
    	//if(autoTimer1.hasPeriodPassed(1000)){
    	//mShooter_1.set(0.0);
    	//mShooter_2.set(0.0);}
    } 
    public void stopBall()
    {
    	mShooter_1.set(0.0);
    	mShooter_2.set(0.0);
    }
    public void moveBall ()
    {
    	mMoveBall.set(xBox2.getRawAxis(4)*(0.2));
    }
    public void wheelOnBack()
    {
    	mWheelOnTheBack.set(1.0);
    }
    public void wheelOnBackStop()
    {
    	mWheelOnTheBack.set(-1.0);
    }
    public void pickUpBall ()
    {	
    	mPickUpBall.set(-0.5);
		
    	//mPickUpBall.set(0.0);
    }
    public void putDownBall()
    {
    
    	mPickUpBall.set(0.5);
    	
    //	mPickUpBall.set(0.0);
    }
    public void stopTouchingTheBall()
    {
    	mPickUpBall.set(0.0);
    }
    public void adjustAngle()
    {
    	mAdjustAngle.set(Relay.Value.kForward);
    }
    public void adjustAngle1()
{
    	mAdjustAngle.set(Relay.Value.kReverse);
}
	public void stopAngle()
	{
		mAdjustAngle.set(Relay.Value.kOff);
	}

 /*   public void centerCalculate() {
		// TODO Auto-generated method stub
		ColorImage image = null;
		BinaryImage thresholdImage = null;
		BinaryImage bigObjectsImage = null;
		BinaryImage convexHullImage = null;
		BinaryImage filteredImage = null;
		try 
		{
			image = technoCam.getImage();
			thresholdImage = image.thresholdRGB(0, 45, 25, 255, 0, 27);
			bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);
			convexHullImage = bigObjectsImage.convexHull(false);
		}catch(Exception ex)
		{
			
		}try 
		{
			thresholdImage.free();
			bigObjectsImage.free();
			convexHullImage.free();
			
		}catch (Exception ex) 
		{}
		finally 
		{}
	}
*/
	public void testPeriodic() {
		JSONObject json;
    	try{
    		json = readJsonFromUrl("http://10.17.58.198:8080/GRIP/data");
    	}catch(Exception e){
    		System.out.printf("Something happended while reading json: %s\r\n", e.getMessage());
    		return;
    	}
    	JSONArray areas;
    	try{
    		areas = json.getJSONObject("myContoursReport").getJSONArray("area");
    	}catch(Exception e){
    		System.out.printf("Could not turn inner object into JSONObject: %s\r\n", e.getMessage());
    		return;
    	}
    	
    	if(areas.length() > 0){
    		tmDrive.tankDrive(0.5, 0.5);
    	}else{
    		tmDrive.tankDrive(0.0, 0.0);
    	}
    	
//    	if(areas.length() > 0 && thrust <= 10)
//    	{
//    		tmDrive.tankDrive(.5, .5);
//    		thrust = 10;
//    	} else if(thrust > 0) {
//    		tmDrive.tankDrive(.5, .5);
//    		thrust--;
//    		System.out.printf("Thrust Value: %d\r\n", thrust);
//    	} else {
//    		tmDrive.tankDrive(0.0, 0.0);
//    	}
    }
    
}

	