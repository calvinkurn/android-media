package com.tokopedia.tkpd.campaign.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.tkpd.campaign.configuration.ShakeDetector;
import com.tokopedia.tkpd.campaign.view.activity.ShakeDetectCampaignActivity;
import com.tokopedia.tkpd.campaign.view.activity.ShakeShakeAudioCampaignActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;

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
    public static final String ACTION_SHAKE_SHAKE_SYNCED = "com.tkpd.action.shake.shake";
    public static final String FIREBASE_SHAKE_SHAKE_REMOTE_CONFIG_KEY = "app_shake_feature_enabled";
    public static final String FIREBASE_SHAKE_SHAKE_AUDIO_REMOTE_CONFIG_KEY = "audio_campaign_is_audio";
    private SharedPreferences sharedPreferences;
    private String NOTIFICATION_SHAKE_SHAKE = Constants.Settings.NOTIFICATION_SHAKE_SHAKE;
    public static final int MESSAGE_ENABLE_SHAKE = 1;
    public static final int MESSAGE_DISABLE_SHAKE = 2;
    public static final int MESSAGE_SHAKE_START = 3;
    public static final int MESSAGE_SHAKE_END = 4;
    public static final int MESSAGE_SHAKE_SHAKE_CONTINUE_LONG = 5;

    public static final int SHAKE_SHAKE_END_TIME_MS = 800;
    public static final int SHAKE_SHAKE_WAIT_FOR_SECOND = 1000;
    public static final int SHAKE_SHAKE_CONTINUE_LONG_TIME_SECOND = 5000;
    private boolean  isShakeShakeEnable = true;

    public static String sTopActivity = null;
    private String mOpenedActivity = null;
    private Activity activity;

    private ShakeDetectManager() {

    }

    public static ShakeDetectManager getShakeDetectManager() {
        return shakeDetectManager;
    }

    public void registerShake(String screenName,Activity activity) {
        if (!screenName.equals(ShakeDetectCampaignActivity.SCREEN_NAME)) {
            mOpenedActivity = screenName;
            this.activity = activity;
        }
        initSettingConfig();
        if (isShakeShakeEnable()) {
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
    boolean isNotificationOn = true;

    private void initSettingConfig() {
        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(mContext);
        isNotificationOn =
                sharedPreferences.getBoolean(NOTIFICATION_SHAKE_SHAKE, false);
    }

    public void disableShakeShake() {
        sharedPreferences.edit().putBoolean(NOTIFICATION_SHAKE_SHAKE, false).apply();
        isNotificationOn = false;
    }

    private void initRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(mContext);
    }

    private boolean isShakeShakeEnable() {
        return remoteConfig.getBoolean(FIREBASE_SHAKE_SHAKE_REMOTE_CONFIG_KEY,true) && isNotificationOn;

    }

    private boolean isAudioShakeEnable() {
        return remoteConfig.getBoolean(FIREBASE_SHAKE_SHAKE_AUDIO_REMOTE_CONFIG_KEY,false);

    }

    private boolean isReactNativeOnReleaseMode() {
        SharedPreferences reactSharedPreferences = mContext.getSharedPreferences(DeveloperOptions.SP_REACT_ENABLE_SHAKE, Context.MODE_PRIVATE);
        return reactSharedPreferences.getBoolean(DeveloperOptions.IS_ENABLE_SHAKE_REACT, true);
    }

    @Override
    public void hearShake() {
        sTopActivity = mOpenedActivity;
        /*if(mShakeEnabler.hasMessages(MESSAGE_ENABLE_SHAKE)) {
            mShakeEnabler.removeMessages(MESSAGE_ENABLE_SHAKE);
            mShakeEnabler.sendEmptyMessageDelayed(MESSAGE_ENABLE_SHAKE,SHAKE_SHAKE_WAIT_FOR_SECOND);
        }*/
        if(mShakeEnabler.hasMessages(MESSAGE_SHAKE_END)) {
            mShakeEnabler.removeMessages(MESSAGE_SHAKE_END);
            mShakeEnabler.sendEmptyMessageDelayed(MESSAGE_SHAKE_END,SHAKE_SHAKE_END_TIME_MS);
            return;
        }

        if (isShakeShakeEnable && isShakeShakeEnable() && isReactNativeOnReleaseMode()) {
           /* mShakeEnabler.sendEmptyMessage(MESSAGE_DISABLE_SHAKE);
            mShakeEnabler.sendEmptyMessageDelayed(MESSAGE_ENABLE_SHAKE,SHAKE_SHAKE_WAIT_FOR_SECOND);*/
            mShakeEnabler.sendEmptyMessageDelayed(MESSAGE_SHAKE_END,SHAKE_SHAKE_END_TIME_MS);
            mShakeEnabler.sendEmptyMessageDelayed(MESSAGE_SHAKE_SHAKE_CONTINUE_LONG,SHAKE_SHAKE_CONTINUE_LONG_TIME_SECOND);

        }
    }

    public void startShake(boolean isLongShake) {
           Intent intent = null;
        if (!isAudioShakeEnable()) {
            intent = ShakeDetectCampaignActivity.getShakeDetectCampaignActivity(mContext,isLongShake);
        } else if(false) { // feature under development
            intent = ShakeShakeAudioCampaignActivity.getCapturedAudioCampaignActivity(mContext);
        }
        if(intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            mContext.registerReceiver(receiver, new IntentFilter(ACTION_SHAKE_SHAKE_SYNCED));
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
               case MESSAGE_SHAKE_END:
                   if(mShakeEnabler.hasMessages(MESSAGE_SHAKE_SHAKE_CONTINUE_LONG)) {
                       mShakeEnabler.removeMessages(MESSAGE_SHAKE_SHAKE_CONTINUE_LONG);
                       startShake(false);
                   }
                   break;
               case MESSAGE_SHAKE_SHAKE_CONTINUE_LONG:
                   startShake(true);
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
            if(intent.getAction() == ACTION_SHAKE_SHAKE_SYNCED) {
                if (intent.getBooleanExtra("isSuccess", false)) {
                    final Intent intent1 = new Intent(context, DeepLinkActivity.class);;
                    if (intent.getStringExtra("data") != null) {

                        Uri uri = Uri.parse("" + intent.getStringExtra("data"));
                        intent1.setData(uri);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if(intent1.resolveActivity(context.getPackageManager()) != null)
                                    RouteManager.route(activity,intent.getStringExtra("data"));

                            }
                        }, 500);
                    }
                } else if (intent.getBooleanExtra("needLogin", false)) {
                    final Intent intent1 = ((TkpdCoreRouter) MainApplication.getAppContext())
                            .getLoginIntent(context);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if(intent1.resolveActivity(context.getPackageManager()) != null)
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
