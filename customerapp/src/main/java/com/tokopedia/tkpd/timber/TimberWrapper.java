package com.tokopedia.tkpd.timber;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.logger.utils.DataLogConfig;
import com.tokopedia.logger.utils.TimberReportingTree;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.sellerapp.BuildConfig;
import com.tokopedia.user.session.UserSession;

import timber.log.Timber;

/**
 * Wrap for timber library
 * Initialize this in application level
 * TimberWrapper.init(application);
 */
public class TimberWrapper {

    public static final String[] LOGENTRIES_TOKEN = new String[]{
            "08fcd148-14aa-4d89-ac67-4f70fefd2f37",
            "60664ea7-4d61-4df1-b39c-365dc647aced"
    };

    private static final String ANDROID_CUSTOMER_APP_LOG_CONFIG = "android_customer_app_log_config";
    
    public static void init(@NonNull Application application){
        initByConfig(application, new FirebaseRemoteConfigImpl(application));
    }

    private static void initByConfig(@NonNull Application application, @NonNull RemoteConfig remoteConfig){
        Timber.uprootAll();
        boolean isDebug = BuildConfig.DEBUG;
        if (isDebug) {
            Timber.plant(new TimberDebugTree());
        } else {
            String logConfigString = remoteConfig.getString(ANDROID_CUSTOMER_APP_LOG_CONFIG);
            if (!TextUtils.isEmpty(logConfigString)) {
                DataLogConfig dataLogConfig = new Gson().fromJson(logConfigString, DataLogConfig.class);
                if(dataLogConfig != null && dataLogConfig.isEnabled() && GlobalConfig.VERSION_CODE >= dataLogConfig.getAppVersionMin()) {
                    UserSession userSession = new UserSession(application);
                    TimberReportingTree timberReportingTree = new TimberReportingTree(dataLogConfig.getTags());
                    timberReportingTree.setUserId(userSession.getUserId());
                    timberReportingTree.setVersionName(GlobalConfig.VERSION_NAME);
                    timberReportingTree.setVersionCode(GlobalConfig.VERSION_CODE);
                    Timber.plant(timberReportingTree);
                }
            }
        }
    }
}
