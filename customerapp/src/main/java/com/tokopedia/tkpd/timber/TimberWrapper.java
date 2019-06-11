package com.tokopedia.tkpd.timber;

import android.app.Application;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.tkpd.BuildConfig;

import timber.log.Timber;

/**
 * Wrap for timber library
 * Initialize this in application level
 * TimberWrapper.init(application);
 */
public class TimberWrapper {
    private static final String ANDROID_CUSTOMER_APP_LOG_CONFIG = "android_customer_app_log_config";

    public static void init(Application application){
        initByConfig(new FirebaseRemoteConfigImpl(application));
    }

    public static void initByConfig(RemoteConfig remoteConfig){
        Timber.uprootAll();
        boolean isDebug = BuildConfig.DEBUG;
        if (isDebug) {
            Timber.plant(new Timber.DebugTree());
        } else {
            String logConfigString = remoteConfig.getString(ANDROID_CUSTOMER_APP_LOG_CONFIG);
            if (!TextUtils.isEmpty(logConfigString)) {
                DataLogConfig dataLogConfig = new Gson().fromJson(logConfigString, DataLogConfig.class);
                if(dataLogConfig != null) {
                    if (GlobalConfig.VERSION_CODE < dataLogConfig.getAppVersionMin()) {
                        Timber.plant(new Timber.DebugTree());
                    } else {
                        Timber.plant(new TimberReportingTree());
                    }
                }
            }
        }
    }
}
