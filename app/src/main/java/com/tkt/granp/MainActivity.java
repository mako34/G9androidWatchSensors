package com.tkt.granp;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    final static int SENSOR_TYPE_GESTURE1 = 80;
    final static int SENSOR_TYPE_GESTURE2 = 81;
    final static int SENSOR_TYPE_GESTURE3 = 82;
    final static int SENSOR_TYPE_GESTURE4 = 83;
    final static int SENSOR_TYPE_CONTEXT_POSTURE = 84;
    final static int SENSOR_TYPE_CONTEXT_MOTION = 85;
    final static int SENSOR_TYPE_CONTEXT_TRANSPORT = 86;

    //


    private SensorManager sensorManager;
    private boolean color = false;
    private View view;
    private long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.textView);
        view.setBackgroundColor(Color.GREEN);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();


// list sensors available
        List<Sensor> listSensor=
                sensorManager.getSensorList(Sensor.TYPE_ALL);

        List<String> listSensorType = new ArrayList<String>();

        for(int i=0; i<listSensor.size(); i++)
        {
            listSensorType.add(listSensor.get(listSensor.size()-1-i).getName());

            Log.d("mk","tomad: "+listSensor.get(listSensor.size()-1-i).getName());

        }

        //

        toaster("ss9");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            getAccelerometer(event);
//        }


Log.d("mk","event:"+event.sensor.getType());
        //

        switch (event.sensor.getType())
        {

//            case Sensor.TYPE_ACCELEROMETER:
//                toaster("accelera");
//                break;

            case SENSOR_TYPE_GESTURE1:
                if ((int)event.values[0] == 1) toaster("Gest1 , EventID= Raise HAND");
                break;
            case SENSOR_TYPE_GESTURE2:
                if ((int)event.values[0] == 1)
                    toaster("Gest2 , EventID= Rotate or Twist HAND");
                break;
            case SENSOR_TYPE_GESTURE3:
                switch((int)event.values[0]) {
                    case 1:
                        toaster("Gest3 , Front TAP ");
                        break;
                    case 2:
                        toaster("Gest3 , Back TAP");
                        break; }
                break;
            case SENSOR_TYPE_GESTURE4:
                if ((int)event.values[0] == 1) toaster("Gest4 , EventID= Free Fall");
                break;
            case SENSOR_TYPE_CONTEXT_POSTURE:
                switch((int)event.values[0]) {
                    case 1:
                        toaster("CP , Unknown ");
                        break;
                    case 2:
                        toaster("CP , In Pocket");
                        break;
                }
                break;
            case SENSOR_TYPE_CONTEXT_MOTION:
                switch((int)event.values[0]) {
                    case 1:
                        toaster("CM , Unknown ");
                        break;
                    case 2:
                        toaster("CM , Stationary");
                        break;
                    case 3:
                        toaster("CM , Not on Person");
                        break;
                    case 4:
                        toaster("CM , Waking Steps="+event.values[1]);
                        break;
                    case 5:
                        toaster("CM , Running Steps="+event.values[1]);
                        break;
                    case 6:
                        toaster("CM , Jogging"); break;
                }
                break;
            case SENSOR_TYPE_CONTEXT_TRANSPORT:
                switch((int)event.values[0]) {
                    case 1:
                        toaster("CT , In Vehicle");
                        break;
                    case 2:
                        toaster("CT , Off Vehicle");
                        break;
                } break;
        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = event.timestamp;
        if (accelationSquareRoot >= 2) //
        {
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
//            Toast.makeText(this, "Device was shuffed", Toast.LENGTH_SHORT).show();

            toaster("shuffled");

            if (color) {
                view.setBackgroundColor(Color.GREEN);
            } else {
                view.setBackgroundColor(Color.RED);
            }
            color = !color;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);

        //testing the others!
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(SENSOR_TYPE_GESTURE1), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(SENSOR_TYPE_GESTURE2), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(SENSOR_TYPE_GESTURE3), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(SENSOR_TYPE_GESTURE4), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(SENSOR_TYPE_CONTEXT_POSTURE), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(SENSOR_TYPE_CONTEXT_MOTION), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(SENSOR_TYPE_CONTEXT_TRANSPORT), SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    void toaster(String text) {
        Toast.makeText(this, "Device was: "+text, Toast.LENGTH_SHORT).show();

    }


}
