package com.tokopedia.tkpd.timber;

import android.app.Application;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.logger.LogWrapper;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.tkpd.BuildConfig;

import java.util.Arrays;

import timber.log.Timber;

/**
 * Wrap for timber library
 * Initialize this in application level
 * TimberWrapper.init(application);
 */
public class TimberWrapper {

    public static final String[] LOGENTRIES_TOKEN = new String[]{
            "08fcd148-14aa-4d89-ac67-4f70fefd2f37",
            "60664ea7-4d61-4df1-b39c-365dc647aced",
            "33acc8e7-1b5c-403e-bd31-7c1e61bbef2c"
    };

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
