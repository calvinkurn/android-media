package com.tokopedia.sellerapp.utils.timber;

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
            "d9acb40d-275d-4281-90ee-fcd69bdf4a91",
            "df93093a-5b2a-4022-b0e3-db67e79b3a33"
    };

    private static final String REMOTE_CONFIG_KEY_LOG = "android_seller_app_log_config";
    
    public static void init(@NonNull Application application){
        initByConfig(application, new FirebaseRemoteConfigImpl(application));
    }

    private static void initByConfig(@NonNull Application application, @NonNull RemoteConfig remoteConfig){
        Timber.uprootAll();
        boolean isDebug = BuildConfig.DEBUG;
        if (isDebug) {
            Timber.plant(new TimberDebugTree());
        } else {
            String logConfigString = remoteConfig.getString(REMOTE_CONFIG_KEY_LOG);
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
