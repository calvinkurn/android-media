package com.tokopedia.tkpd.timber;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.logger.LogManager;
import com.tokopedia.logger.model.ScalyrConfig;
import com.tokopedia.logger.utils.DataLogConfig;
import com.tokopedia.logger.utils.ScalyrUtils;
import com.tokopedia.logger.utils.TimberReportingTree;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Wrap for timber library
 * Initialize this in application level
 * TimberWrapper.init(application);
 */
public class TimberWrapper {
    private static final String REGEX_ALPHA_NUMERIC = "[^a-zA-Z0-9]";
    private static final int PART_DEVICE_ID_LENGTH = 9;

    private static final String[] LOGENTRIES_TOKEN = new String[]{
            new String(new char[]{
                    48, 56, 102, 99, 100, 49, 52, 56, 45, 49, 52, 97, 97, 45, 52, 100, 56, 57, 45,
                    97, 99, 54, 55, 45, 52, 102, 55, 48, 102, 101, 102, 100, 50, 102, 51, 55
            }),
            new String(new char[]{
                    54, 48, 54, 54, 52, 101, 97, 55, 45, 52, 100, 54, 49, 45, 52, 100, 102, 49, 45,
                    98, 51, 57, 99, 45, 51, 54, 53, 100, 99, 54, 52, 55, 97, 99, 101, 100
            }),
    };

    private static final String[] SCALYR_TOKEN = new String[]{
            new String(new char[]{
                    48, 73, 89, 118, 95, 77, 81, 105, 88, 74, 97, 65, 97, 89, 89, 75, 71, 101, 56,
                    48, 57, 117, 109, 49, 109, 77, 51, 75, 117, 106, 85, 69, 65, 89, 56, 65, 75,
                    101, 70, 75, 97, 72, 122, 56, 45
            }),
            new String(new char[]{
                    48, 115, 83, 66, 113, 56, 78, 110, 121, 89, 53, 114, 85, 56, 78, 56, 90, 104,
                    110, 83, 110, 85, 74, 55, 103, 111, 53, 50, 85, 98, 76, 69, 71, 66, 98, 53,
                    116, 102, 121, 77, 68, 77, 77, 119, 45
            })
    };

    private static final String ANDROID_CUSTOMER_APP_LOG_CONFIG = "android_customer_app_log_config";

    public static void init(Application application) {
        LogManager.init(application);
        if (LogManager.instance != null) {
            LogManager.setLogentriesToken(TimberWrapper.LOGENTRIES_TOKEN);
            LogManager.setScalyrConfigList(getScalyrConfigList(application));
        }
        initConfig(application);
    }

    public static void initConfig(@NonNull Application application){
        initByRemoteConfig(application, new FirebaseRemoteConfigImpl(application));
    }

    public static void initByRemoteConfig(@NonNull Application application, @NonNull RemoteConfig remoteConfig){
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

    private static List<ScalyrConfig> getScalyrConfigList(Context context) {
        List<ScalyrConfig> scalyrConfigList = new ArrayList<>();
        for (int i = 0; i < SCALYR_TOKEN.length; i++) {
            scalyrConfigList.add(getScalyrConfig(context, SCALYR_TOKEN[i], i+1));
        }
        return scalyrConfigList;
    }

    private static ScalyrConfig getScalyrConfig(Context context, String token, int priority) {
        String session = ScalyrUtils.INSTANCE.getLogSession(context, priority);
        String source = String.format("android-main-app-p%s", priority);
        String parser = String.format("android-main-app-p%s-parser", priority);
        return new ScalyrConfig(token, session, source, parser);
    }
}
