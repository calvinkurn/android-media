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
                    48, 56, 102, 99, 100, 49, 52, 56, 45, 49, 52, 97, 97, 45, 52, 100, 56, 57, 45,
                    97, 99, 54, 55, 45, 52, 102, 55, 48, 102, 101, 102, 100, 50, 102, 51, 55
            }),
            new String(new char[]{
                    54, 48, 54, 54, 52, 101, 97, 55, 45, 52, 100, 54, 49, 45, 52, 100, 102, 49, 45,
                    98, 51, 57, 99, 45, 51, 54, 53, 100, 99, 54, 52, 55, 97, 99, 101, 100
            }),
    };

    private static final String SCALYR_TOKEN = new String(new char[]{
            48, 73, 89, 118, 95, 77, 81, 105, 88, 74, 97, 65, 97, 89, 89, 75, 71, 101, 56,
            48, 57, 117, 109, 49, 109, 77, 51, 75, 117, 106, 85, 69, 65, 89, 56, 65, 75,
            101, 70, 75, 97, 72, 122, 56, 45
    });

    private static final String REMOTE_CONFIG_KEY_LOG = "android_customer_app_log_config";

    private static final Object LOCK = new Object();

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
        boolean isDebug = GlobalConfig.DEBUG;
        if (isDebug) {
            plantNewTree(new TimberDebugTree());
        } else {
            plantTimberReleaseTree(context, remoteConfig);
        }
    }

    private static void plantTimberReleaseTree(Context context, @NonNull RemoteConfig remoteConfig) {
        try {
            String logConfigString = remoteConfig.getString(REMOTE_CONFIG_KEY_LOG);
            if (!TextUtils.isEmpty(logConfigString)) {
                DataLogConfig dataLogConfig = new Gson().fromJson(logConfigString, DataLogConfig.class);
                if (dataLogConfig != null && dataLogConfig.isEnabled() && GlobalConfig.VERSION_CODE >= dataLogConfig.getAppVersionMin() && dataLogConfig.getTags() != null) {
                    UserSession userSession = new UserSession(context);
                    TimberReportingTree timberReportingTree = new TimberReportingTree(dataLogConfig.getTags());
                    timberReportingTree.setUserId(userSession.getUserId());
                    timberReportingTree.setPartDeviceId(LoggerUtils.INSTANCE.getPartDeviceId(context));
                    timberReportingTree.setVersionName(GlobalConfig.VERSION_NAME);
                    timberReportingTree.setVersionCode(GlobalConfig.VERSION_CODE);
                    timberReportingTree.setClientLogs(dataLogConfig.getClientLogs());
                    timberReportingTree.setQueryLimits(dataLogConfig.getQueryLimits());
                    plantNewTree(timberReportingTree);
                }
            }
        } catch (Throwable throwable) {
            // do nothing
        }
    }

    private static void plantNewTree(Timber.DebugTree tree){
        synchronized (LOCK) {
            Timber.uprootAll();
            Timber.plant(tree);
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
        String serverHost = String.format("android-main-app-p%s", priority);
        String parser = String.format("android-main-app-p%s-parser", priority);
        return new ScalyrConfig(SCALYR_TOKEN, session, serverHost, parser, context.getPackageName(), GlobalConfig.DEBUG, priority);
    }
}
