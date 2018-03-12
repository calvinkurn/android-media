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
import android.os.Message;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.tkpd.campaign.configuration.ShakeDetector;
import com.tokopedia.tkpd.campaign.view.activity.ShakeShakeAudioCampaignActivity;
import com.tokopedia.tkpd.campaign.view.activity.ShakeDetectCampaignActivity;

import java.util.List;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by sandeepgoyal on 20/02/18.
 */

public class ShakeDetectManager implements ShakeDetector.Listener {
    private static ShakeDetectManager shakeDetectManager = new ShakeDetectManager();
    ShakeDetector sd;
    private Context mContext;
    private RemoteConfig remoteConfig;
    private SensorManager sensorManager;
    public static String ACTION_SHAKE_SHAKE_SYNCED = "com.tkpd.action.shake.shake";

    public static final String FIREBASE_SHAKE_SHAKE_REMOTE_CONFIG_KEY = "app_shake_feature_enabled";
    public static final String FIREBASE_SHAKE_SHAKE_AUDIO_REMOTE_CONFIG_KEY = "audio_campaign_is_audio";

    public static final int MESSAGE_ENABLE_SHAKE = 1;
    public static final int MESSAGE_DISABLE_SHAKE = 2;

    public static final int SHAKE_SHAKE_WAIT_FOR_SECOND = 2000;
    private boolean  isShakeShakeEnable = true;

    public static String sTopActivity = null;

    private ShakeDetectManager() {

    }

    public static ShakeDetectManager getShakeDetectManager() {
        return shakeDetectManager;
    }

    public void registerShake(String screenName) {
        sTopActivity = screenName;
        if(isShakeShakeEnable()) {
            sd.registerListener(this);
            sd.start(sensorManager);
        }
    }

    public void unregisterShake() {
        sd.unregisterListener(this);
        sd.stop();
    }


    public void init() {
        if(sd == null) {
            mContext = MainApplication.getAppContext();
            sd = new ShakeDetector();
            sensorManager = (SensorManager)mContext.getSystemService(SENSOR_SERVICE);
            initRemoteConfig();
        }
    }


    private void initRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(mContext);
    }

    private boolean isShakeShakeEnable() {
        return remoteConfig.getBoolean(FIREBASE_SHAKE_SHAKE_REMOTE_CONFIG_KEY,true);

    }

    private boolean isAudioShakeEnable() {
        return remoteConfig.getBoolean(FIREBASE_SHAKE_SHAKE_AUDIO_REMOTE_CONFIG_KEY,false);

    }


    @Override
    public void hearShake() {
        if(mShakeEnabler.hasMessages(MESSAGE_ENABLE_SHAKE)) {
            mShakeEnabler.removeMessages(MESSAGE_ENABLE_SHAKE);
            mShakeEnabler.sendEmptyMessageDelayed(MESSAGE_ENABLE_SHAKE,SHAKE_SHAKE_WAIT_FOR_SECOND);
        }

        if (isShakeShakeEnable && isShakeShakeEnable()) {
            mShakeEnabler.sendEmptyMessage(MESSAGE_DISABLE_SHAKE);
            mShakeEnabler.sendEmptyMessageDelayed(MESSAGE_ENABLE_SHAKE,SHAKE_SHAKE_WAIT_FOR_SECOND);
            Intent intent = null;
            if (!isAudioShakeEnable()) {
                intent = ShakeDetectCampaignActivity.getShakeDetectCampaignActivity(mContext);
            } else if(false) { // feature under development
                intent = ShakeShakeAudioCampaignActivity.getCapturedAudioCampaignActivity(mContext);
            }
            if(intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                mContext.registerReceiver(receiver, new IntentFilter(ACTION_SHAKE_SHAKE_SYNCED));
            }
        }
    }

    Handler mShakeEnabler = new Handler() {
        public void handleMessage(Message m) {
           switch (m.what) {
               case MESSAGE_ENABLE_SHAKE:
                   isShakeShakeEnable = true;
                   break;
               case MESSAGE_DISABLE_SHAKE:
                   isShakeShakeEnable = false;
                   break;
           }
        }
    };


    public void deinit() {
        mContext.unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getBooleanExtra("isSuccess", false)) {
                final Intent intent1 = new Intent(Intent.ACTION_VIEW);
                if (intent.getStringExtra("data") != null) {
                    Uri uri = Uri.parse("" + intent.getStringExtra("data"));
                    intent1.setData(uri);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);
                        }
                    }, 500);
                }
            }
            deinit();
        }
    };




    public void onDestroy() {
        sd.unregisterListener(this);
        sd.stop();
    }
}
