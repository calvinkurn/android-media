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
import com.tokopedia.user.session.UserSession;

import timber.log.Timber;

/**
 * Wrap for timber library
 * Initialize this in application level
 * TimberWrapper.init(application);
 */
public class TimberWrapper {
    private static final String REGEX_ALPHA_NUMERIC = "[^a-zA-Z0-9]";
    private static final int PART_DEVICE_ID_LENGTH = 9;

    public static final String[] LOGENTRIES_TOKEN = new String[]{
            "08fcd148-14aa-4d89-ac67-4f70fefd2f37",
            "60664ea7-4d61-4df1-b39c-365dc647aced"
    };

    public static final String[] SCALYR_TOKEN = new String[]{
            new String(new char[]{
                    48, 115, 98, 97, 53, 108, 77, 56, 69, 49,
                    112, 121, 115, 110, 71, 86, 111, 66, 111, 108,
                    82, 67, 53, 109, 83, 51, 109, 98, 101, 95,
                    101, 110, 68, 110, 118, 107, 116, 98, 80, 78,
                    95, 100, 84, 77, 45
            })
    };

    private static final String ANDROID_CUSTOMER_APP_LOG_CONFIG = "android_customer_app_log_config";
    
    public static void init(@NonNull Application application){
        initByConfig(application, new FirebaseRemoteConfigImpl(application));
    }

    public static void initByConfig(@NonNull Application application, @NonNull RemoteConfig remoteConfig){
        Timber.uprootAll();
        boolean isDebug = GlobalConfig.DEBUG;
        if (isDebug) {
            Timber.plant(new TimberDebugTree());
        } else {
            String logConfigString = remoteConfig.getString(ANDROID_CUSTOMER_APP_LOG_CONFIG);
            if (!TextUtils.isEmpty(logConfigString)) {
                DataLogConfig dataLogConfig = new Gson().fromJson(logConfigString, DataLogConfig.class);
                if(dataLogConfig != null && dataLogConfig.isEnabled() && GlobalConfig.VERSION_CODE >= dataLogConfig.getAppVersionMin() && dataLogConfig.getTags() != null) {
                    UserSession userSession = new UserSession(application);
                    TimberReportingTree timberReportingTree = new TimberReportingTree(dataLogConfig.getTags());
                    timberReportingTree.setUserId(userSession.getUserId());
                    timberReportingTree.setPartDeviceId(getPartDeviceId(userSession.getDeviceId()));
                    timberReportingTree.setVersionName(GlobalConfig.VERSION_NAME);
                    timberReportingTree.setVersionCode(GlobalConfig.VERSION_CODE);
                    timberReportingTree.setClientLogs(dataLogConfig.getClientLogs());
                    Timber.plant(timberReportingTree);
                }
            }
        }
    }

    private static String getPartDeviceId(String deviceId) {
        deviceId = deviceId.replaceAll(REGEX_ALPHA_NUMERIC, "");
        if (deviceId.length() > PART_DEVICE_ID_LENGTH) {
            deviceId = deviceId.substring(deviceId.length() - PART_DEVICE_ID_LENGTH);
        }
        return deviceId;
    }
}
