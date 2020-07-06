package com.tokopedia.sellerapp.utils.timber;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.logger.LogManager;
import com.tokopedia.logger.model.ScalyrConfig;
import com.tokopedia.logger.utils.DataLogConfig;
import com.tokopedia.logger.utils.LoggerUtils;
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

    private static final int PRIORITY_LENGTH = 2;

    private static final String[] LOGENTRIES_TOKEN = new String[]{
            new String(new char[]{
                    100, 57, 97, 99, 98, 52, 48, 100, 45, 50, 55, 53, 100, 45, 52, 50, 56, 49, 45,
                    57, 48, 101, 101, 45, 102, 99, 100, 54, 57, 98, 100, 102, 52, 97, 57, 49
            }),
            new String(new char[]{
                    100, 102, 57, 51, 48, 57, 51, 97, 45, 53, 98, 50, 97, 45, 52, 48, 50, 50, 45,
                    98, 48, 101, 51, 45, 100, 98, 54, 55, 101, 55, 57, 98, 51, 97, 51, 51
            }),
    };

    private static final String SCALYR_TOKEN = new String(new char[]{
            48, 73, 89, 47, 83, 70, 70, 107, 72, 74, 50, 110, 98, 97, 112, 80, 78, 97, 100,
            76, 80, 84, 88, 113, 70, 115, 69, 82, 71, 69, 103, 49, 87, 66, 49, 121, 49,
            119, 120, 81, 53, 119, 51, 115, 45
    });

    private static final String REMOTE_CONFIG_KEY_LOG = "android_seller_app_log_config";

    public static void init(Application application) {
        LogManager.init(application);
        if (LogManager.instance != null) {
            LogManager.setLogentriesToken(TimberWrapper.LOGENTRIES_TOKEN);
            LogManager.setScalyrConfigList(getScalyrConfigList(application));
        }
        initConfig(application);
    }

    public static void initConfig(@NonNull Context context){
        initByRemoteConfig(context, new FirebaseRemoteConfigImpl(context));
    }

    public static void initByRemoteConfig(@NonNull Context context, @NonNull RemoteConfig remoteConfig){
        Timber.uprootAll();
        boolean isDebug = GlobalConfig.DEBUG;
        if (isDebug) {
            Timber.plant(new TimberDebugTree());
        } else {
            String logConfigString = remoteConfig.getString(REMOTE_CONFIG_KEY_LOG);
            if (!TextUtils.isEmpty(logConfigString)) {
                DataLogConfig dataLogConfig = new Gson().fromJson(logConfigString, DataLogConfig.class);
                if(dataLogConfig != null && dataLogConfig.isEnabled() && GlobalConfig.VERSION_CODE >= dataLogConfig.getAppVersionMin() && dataLogConfig.getTags() != null) {
                    UserSession userSession = new UserSession(context);
                    TimberReportingTree timberReportingTree = new TimberReportingTree(dataLogConfig.getTags());
                    timberReportingTree.setUserId(userSession.getUserId());
                    timberReportingTree.setPartDeviceId(LoggerUtils.INSTANCE.getPartDeviceId(context));
                    timberReportingTree.setVersionName(GlobalConfig.VERSION_NAME);
                    timberReportingTree.setVersionCode(GlobalConfig.VERSION_CODE);
                    timberReportingTree.setClientLogs(dataLogConfig.getClientLogs());
                    timberReportingTree.setQueryLimits(dataLogConfig.getQueryLimits());
                    Timber.plant(timberReportingTree);
                }
            }
        }
    }

    private static List<ScalyrConfig> getScalyrConfigList(Context context) {
        List<ScalyrConfig> scalyrConfigList = new ArrayList<>();
        for (int i = 0; i < PRIORITY_LENGTH; i++) {
            scalyrConfigList.add(getScalyrConfig(context, i+1));
        }
        return scalyrConfigList;
    }

    private static ScalyrConfig getScalyrConfig(Context context, int priority) {
        String session = LoggerUtils.INSTANCE.getLogSession(context);
        String serverHost = String.format("android-seller-app-p%s", priority);
        String parser = String.format("android-seller-app-p%s-parser", priority);
        return new ScalyrConfig(SCALYR_TOKEN, session, serverHost, parser, context.getPackageName(), GlobalConfig.DEBUG, priority);
    }
}