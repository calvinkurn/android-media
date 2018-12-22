package com.tokopedia.core.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core2.R;
import com.tokopedia.core.app.MainApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tkpd_Eka on 3/6/2015.
 */
public class HUDIntent extends Service {
    TextView tex;
    private View logView;
    public static boolean isRunning = false;
    private static Intent instance;
    private List<String> messageList;
    private List<String> completeMessageList;
    private final IBinder binder = new HUDBinder();
    public HUDValue settings;

    public static class HUDValue{
        public int totalLog;
        public boolean showClass;
    }

    public interface HUDInterface{
        void onServiceConnected(HUDIntent service, ServiceConnection connection);
        void onServiceDisconnected();
    }

    public HUDIntent(){
    }

    public static Intent getInstance(Context context){
        if(instance == null) {
            instance = new Intent(context, HUDIntent.class);
        }
        return instance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate(){
        toaster("Create service");
        initVar();
    }

    private void initVar(){
        tex = new TextView(getBaseContext());
        logView = new View(getBaseContext());
        View.inflate(getBaseContext(), R.layout.frame_logger, null);
        logView.invalidate();

        completeMessageList = new ArrayList<>();
        messageList = new ArrayList<>();
        settings = new HUDValue();
        settings.totalLog = 5;

        tex.setGravity(Gravity.RIGHT);
        tex.setTextColor(Color.BLACK);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toaster("onDestroy");
    }


    public void destroyView(){
        WindowManager window =(WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        window.removeView(tex);
        window.removeView(logView);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addLogToView();
        printMessage("Log initiated");
        isRunning = true;
        MainApplication.bindHudService();
        return START_NOT_STICKY;
    }

    public void printClassName(String param){
        if(settings.showClass)
            printMessage(param);
    }

    public void printMessage(String message){
        if(!isRunning) return;

        try {
            if(messageList.size()<settings.totalLog)
                messageList.add(message);
            else{
                while(messageList.size()>=settings.totalLog){
                    messageList.remove(0);
                }
                messageList.add(message);
            }

            completeMessageList.add(message);

            tex.setText(getCompiledMessage(messageList));
            tex.invalidate();
        } catch (Exception e) {
        }
    }

    private String getCompiledMessage(List<String> message){
        String compiled = message.get(0);
        for(int i = 1; i < message.size() ; i++){
            compiled = compiled + "\n" + message.get(i);
        }
        return compiled;
    }

    public String getCompleteLog(){
        return getCompiledMessage(completeMessageList);
    }

    private void addLogToView(){
        WindowManager windowManager = (WindowManager)getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, 0, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.RIGHT;
        params.setTitle("Load Average");
        windowManager.addView(tex, params);
        windowManager.addView(logView, params);
    }

    public static void startService(Context context){
        if(!isRunning) {
            isRunning = true;
            Intent intent = getInstance(context);
            context.startService(intent);
        }
    }

    public static void stopService(Context context){
        if(isRunning) {
            isRunning = false;
            context.stopService(getInstance(context));
        }
    }

    public static void bindService(final Context context, final HUDInterface listener){
        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                HUDIntent.HUDBinder binder = (HUDIntent.HUDBinder) service;
                HUDIntent intent = binder.getService();
                onConnectedSettings(intent);
                listener.onServiceConnected(intent, this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                listener.onServiceDisconnected();
                toaster(context, "CRASHED");
            }
        };

        if(HUDIntent.isRunning)
            context.bindService(getInstance(context), connection, Context.BIND_AUTO_CREATE);

    }

    private static void onConnectedSettings(HUDIntent intent){
    }

    public static void unbindService(Context context, ServiceConnection connection){
        if(isRunning && connection != null) {
            context.unbindService(connection);
        }
    }

    public class HUDBinder extends Binder{
        public HUDIntent getService(){
            return HUDIntent.this;
        }
    }

    private static void toaster(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    private void toaster(String text){
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }

}