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
            new String(new char[]{
                    48, 56, 102, 99, 100, 49, 52, 56, 45, 49, 52, 97, 97, 45, 52, 100, 56, 57, 45,
                    97, 99, 54, 55, 45, 52, 102, 55, 48, 102, 101, 102, 100, 50, 102, 51, 55
            }),
            new String(new char[]{
                    54, 48, 54, 54, 52, 101, 97, 55, 45, 52, 100, 54, 49, 45, 52, 100, 102, 49, 45,
                    98, 51, 57, 99, 45, 51, 54, 53, 100, 99, 54, 52, 55, 97, 99, 101, 100
            }),
    };

    public static final String[] SCALYR_TOKEN = new String[]{
            new String(new char[]{
                    48, 115, 98, 97, 53, 108, 77, 56, 69, 49, 112, 121, 115, 110, 71, 86, 111, 66,
                    111, 108, 82, 67, 53, 109, 83, 51, 109, 98, 101, 95, 101, 110, 68, 110, 118,
                    107, 116, 98, 80, 78, 95, 100, 84, 77, 45
            }),
            new String(new char[]{
                    48, 115, 98, 97, 53, 108, 77, 56, 69, 49, 112, 121, 115, 110, 71, 86, 111, 66,
                    111, 108, 82, 67, 53, 109, 83, 51, 109, 98, 101, 95, 101, 110, 68, 110, 118,
                    107, 116, 98, 80, 78, 95, 100, 84, 77, 45
            }),
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
                    timberReportingTree.setQueryLimits(dataLogConfig.getQueryLimits());
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
