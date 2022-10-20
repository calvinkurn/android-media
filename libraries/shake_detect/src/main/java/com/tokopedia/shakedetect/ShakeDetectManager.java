package com.tokopedia.shakedetect;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import static android.content.Context.SENSOR_SERVICE;

import java.lang.ref.WeakReference;

public class ShakeDetectManager implements ShakeDetector.Listener {

    private static String SHAKE_DETECT_CAMPAIGN_ACTIVITY_SCREEN_NAME = "ShakeDetectCampaignActivity";
    private static final String SP_REACT_ENABLE_SHAKE = "SP_REACT_ENABLE_SHAKE";
    private static final String IS_ENABLE_SHAKE_REACT = "IS_ENABLE_SHAKE_REACT";

    private static ShakeDetectManager shakeDetectManager = new ShakeDetectManager();
    ShakeDetector sd;
    private Context mContext;
    private Callback callback;
    private RemoteConfig remoteConfig;
    private SensorManager sensorManager;
    public static final String FIREBASE_SHAKE_SHAKE_REMOTE_CONFIG_KEY = "app_shake_feature_enabled";
    public static final String FIREBASE_SHAKE_SHAKE_AUDIO_REMOTE_CONFIG_KEY = "audio_campaign_is_audio";
    private SharedPreferences sharedPreferences;
    private String NOTIFICATION_SHAKE_SHAKE = "notification_shake_shake";
    public static final int MESSAGE_SHAKE_END = 4;
    public static final int MESSAGE_SHAKE_SHAKE_CONTINUE_LONG = 5;

    public static final int SHAKE_SHAKE_END_TIME_MS = 800;
    public static final int SHAKE_SHAKE_WAIT_FOR_SECOND = 1000;
    public static final int SHAKE_SHAKE_CONTINUE_LONG_TIME_SECOND = 5000;

    public static String sTopActivity = "";
    private String mOpenedActivity = "";
    private WeakReference<Activity> activityRef;

    private ShakeDetectManager() {

    }

    public static ShakeDetectManager getShakeDetectManager() {
        return shakeDetectManager;
    }

    public void registerShake(String screenName, Activity activity) {
        initSettingConfig();
        if (isShakeShakeEnable()) {
            sd.registerListener(this);
            sd.start(sensorManager);
            if (!screenName.equals(SHAKE_DETECT_CAMPAIGN_ACTIVITY_SCREEN_NAME)) {
                mOpenedActivity = screenName;
                this.activityRef = new WeakReference<>(activity);
            }
        }

    }

    public void unregisterShake() {
        sd.unregisterListener(this);
        sd.stop();
    }


    public void init(Context context, Callback callback) {
        if (sd == null) {
            mContext = context;
            this.callback = callback;
            sd = new ShakeDetector();
            sensorManager = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);
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

    private RemoteConfig getRemoteConfig() {
        if (remoteConfig == null) {
            remoteConfig = new FirebaseRemoteConfigImpl(mContext);
        }
        return remoteConfig;
    }

    private boolean isShakeShakeEnable() {
        return getRemoteConfig().getBoolean(FIREBASE_SHAKE_SHAKE_REMOTE_CONFIG_KEY, true) && isNotificationOn;

    }

    private boolean isAudioShakeEnable() {
        return getRemoteConfig().getBoolean(FIREBASE_SHAKE_SHAKE_AUDIO_REMOTE_CONFIG_KEY, false);

    }

    private boolean isReactNativeOnReleaseMode() {
        SharedPreferences reactSharedPreferences = mContext.getSharedPreferences(SP_REACT_ENABLE_SHAKE, Context.MODE_PRIVATE);
        return reactSharedPreferences.getBoolean(IS_ENABLE_SHAKE_REACT, true);
    }

    @Override
    public void hearShake() {
        sTopActivity = mOpenedActivity;

        if (mShakeEnabler.hasMessages(MESSAGE_SHAKE_END)) {
            mShakeEnabler.removeMessages(MESSAGE_SHAKE_END);
            mShakeEnabler.sendEmptyMessageDelayed(MESSAGE_SHAKE_END, SHAKE_SHAKE_END_TIME_MS);
            return;
        }

        if (isShakeShakeEnable() && isReactNativeOnReleaseMode()) {
            mShakeEnabler.sendEmptyMessageDelayed(MESSAGE_SHAKE_END, SHAKE_SHAKE_END_TIME_MS);
            mShakeEnabler.sendEmptyMessageDelayed(MESSAGE_SHAKE_SHAKE_CONTINUE_LONG, SHAKE_SHAKE_CONTINUE_LONG_TIME_SECOND);
        }
    }

    public void startShake(boolean isLongShake) {
        callback.onShakeDetected(isLongShake);
    }

    Handler mShakeEnabler = new Handler() {
        public void handleMessage(Message m) {
            switch (m.what) {
                case MESSAGE_SHAKE_END:
                    if (mShakeEnabler.hasMessages(MESSAGE_SHAKE_SHAKE_CONTINUE_LONG)) {
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

    public void onDestroy(String screenName, Activity activity) {
        if (activityRef == null) {
            return;
        }
        Activity act = activityRef.get();
        if (!screenName.equals(mOpenedActivity) && (act != null && act.equals(activity))) {
            mOpenedActivity = null;
            this.activityRef = null;
        }

    }

    public interface Callback {
        void onShakeDetected(boolean isLongShake);
    }
}
