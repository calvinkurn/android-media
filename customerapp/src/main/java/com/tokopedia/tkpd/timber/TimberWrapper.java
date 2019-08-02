package com.tokopedia.tkpd.timber;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.logger.LogWrapper;
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

    public static void init(@NonNull Application application){
        initByConfig(new FirebaseRemoteConfigImpl(application));
    }

    public static void initByConfig(@NonNull RemoteConfig remoteConfig){
        Timber.uprootAll();
        boolean isDebug = BuildConfig.DEBUG;
        if (isDebug) {
            Timber.plant(new TimberDebugTree());
        } else {
            String logConfigString = remoteConfig.getString(ANDROID_CUSTOMER_APP_LOG_CONFIG);
            if (!TextUtils.isEmpty(logConfigString)) {
                DataLogConfig dataLogConfig = new Gson().fromJson(logConfigString,
                        DataLogConfig.class);
                if(dataLogConfig != null) {
                    if (dataLogConfig.isEnabled() &&
                            GlobalConfig.VERSION_CODE >= dataLogConfig.getAppVersionMin()) {
                        Timber.plant(new TimberReportingTree(dataLogConfig.getPriorityList()));
                    }
                }
            }
        }
    }
}
