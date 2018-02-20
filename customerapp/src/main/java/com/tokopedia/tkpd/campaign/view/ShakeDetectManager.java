package com.tokopedia.tkpd.campaign.view;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;

import com.tokopedia.tkpd.campaign.configuration.ShakeDetector;
import com.tokopedia.tkpd.campaign.view.activity.CapturedAudioCampaignActivity;
import com.tokopedia.tkpd.campaign.view.activity.ShakeDetectCampaignActivity;

import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by sandeepgoyal on 20/02/18.
 */

public class ShakeDetectManager implements ShakeDetector.Listener{
    private static ShakeDetectManager shakeDetectManager ;//= new ShakeDetectManager();
    ShakeDetector sd;
    private Context mContext;
    public static String ACTION_SHAKE_SHAKE_SYNCED = "com.tkpd.action.shake.shake";
    private ShakeDetectManager(Context context) {
        mContext = context;
    }

    public static ShakeDetectManager getShakeDetectManager(Context context) {
        shakeDetectManager = new ShakeDetectManager(context);
        return shakeDetectManager;
    }

    public void init() {
        sd = new ShakeDetector();
        sd.registerListener(this);
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
        sd.start(sensorManager);
        sd = new ShakeDetector();
    }

    @Override
    public void hearShake() {
        if(!isAppIsInBackground(mContext)) {
            if (true) {
                mContext.startActivity(ShakeDetectCampaignActivity.getShakeDetectCampaignActivity(mContext));
                mContext.registerReceiver(receiver, new IntentFilter(ACTION_SHAKE_SHAKE_SYNCED));
            } else {
                mContext.startActivity(CapturedAudioCampaignActivity.getCapturedAudioCampaignActivity(mContext));
            }
        }
    }

    public void deinit() {
        mContext.unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if(intent.getBooleanExtra("isSuccess",false)) {
                final Intent intent1 = new Intent(Intent.ACTION_VIEW);
                if(intent.getStringExtra("data") !=null) {
                    Uri uri = Uri.parse("" + intent.getStringExtra("data"));
                    intent1.setData(uri);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            context.startActivity(intent1);
                        }
                    }, 500);
                }
            }
            deinit();
        }
    };

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
