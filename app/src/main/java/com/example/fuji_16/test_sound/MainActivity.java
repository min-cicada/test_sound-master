package com.example.fuji_16.test_sound;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity implements View.OnClickListener{
    private SoundSwitch mSoundSwitch;
    private Handler mHandler = new Handler();
    private TextView maintext;
    private int flag = 0;
    private Button start_button;
    private Button stop_button;
    private boolean rokuon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        short max =0;
        super.onCreate(savedInstanceState);
        mTimer.schedule(mTimerTask,0,500);



        setContentView(R.layout.activity_main);

        start_button = (Button)findViewById(R.id.start_button);
        start_button.setOnClickListener(this);

        stop_button = (Button)findViewById(R.id.stop_button);
        stop_button.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {//ボタンが押された処理すべて
        TextView textView=(TextView)findViewById(R.id.mainText);
        TextView textView2=(TextView)findViewById(R.id.SouonText);//デバック用
        //TextView textView3=(TextView)findViewById(R.id.debug);//デバック用

        switch(v.getId()) {
            case R.id.start_button:    //開始ボタン(id=startbutton)が押された時
                if(rokuon != true) {
                    onResume();
                }
                textView.setText("適音開始");
                textView2.setText("騒音量:"+SoundSwitch.max+"");
                //textView3.setText(String.valueOf(rokuon));
                break;
            case R.id.stop_button:    //終了ボタン(id=stopbutton)が押された時
                if (rokuon == true) {
                    onPause();
                }
                textView.setText("適音終了");
                textView2.setText("騒音量:"+SoundSwitch.max+"");
               // textView3.setText(String.valueOf(flag));
                break;

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mSoundSwitch = new SoundSwitch();// リスナーを登録して音を感知できるように
        rokuon = mSoundSwitch.start();
        //mTimer.schedule(mTimerTask,0,500);
        mSoundSwitch.setOnVolumeReachedListener(
                /*この辺りが古川わからないです助けて*/
                new SoundSwitch.OnReachedVolumeListener() {// 音を感知したら呼び出される
                    public void onReachedVolume(short volume)
                    {//別スレッドからUIスレッドに要求するのでHandler.postでエラー回避
                        mHandler.post(new Runnable()
                        {//Runnableに入った要求を順番にLoopでrunを呼び出し処理
                            public void run() {
                                //
                                // textView2.setText("デシベル");

                                //ここにもともと画面の色を変えるプログラムが一行あった
                            }
                        });
                    }
                /*この辺りが古川わからないです助けて*/
                }
        );
        new Thread(mSoundSwitch).start();// 別スレッドとしてSoundSwitchを開始（録音を開始）
    }
    @Override
    public void onPause() {

        //Activityの状態がonPauseの時の処理
        super.onPause();//superクラスのonPauseを呼び出す
        rokuon= mSoundSwitch.stop();// 録音を停止
    }

    public class MainTimeTask extends TimerTask{
        @Override
        public void run(){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    TextView textView5=(TextView)findViewById(R.id.SouonText);//デバック用
                    textView5.setText("騒音量:"+SoundSwitch.max+"");
                }
            });
        }
    }
    Timer mTimer= new Timer();
    TimerTask mTimerTask=new MainTimeTask();
    Handler getmHandler = new Handler();

}