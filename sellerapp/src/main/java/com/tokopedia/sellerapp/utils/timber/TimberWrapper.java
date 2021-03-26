package com.tokopedia.sellerapp.utils.timber;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.keys.Keys;
import com.tokopedia.logger.LogManager;
import com.tokopedia.logger.model.scalyr.ScalyrConfig;
import com.tokopedia.logger.utils.DataLogConfig;
import com.tokopedia.logger.utils.LoggerUtils;
import com.tokopedia.logger.utils.TimberReportingTree;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.user.session.UserSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrap for timber library
 * Initialize this in application level
 * TimberWrapper.init(application);
 */
public class TimberWrapper {

    private static final int PRIORITY_LENGTH = 2;

    private static final String REMOTE_CONFIG_SCALRY_KEY_LOG = "android_sellerapp_log_config_scalyr";
    private static final String REMOTE_CONFIG_NEW_RELIC_KEY_LOG = "android_sellerapp_log_config_new_relic";

    public static void init(Application application) {
        LogManager.init(application);
        if (LogManager.instance != null) {
            LogManager.setScalyrConfigList(getScalyrConfigList(application));
        }
        initConfig(application);
    }

    public static void initConfig(@NonNull Context context) {
        initByRemoteConfig(context, new FirebaseRemoteConfigImpl(context));
    }

    public static void initByRemoteConfig(@NonNull Context context, @NonNull RemoteConfig remoteConfig) {
        String logScalyrConfigString = remoteConfig.getString(REMOTE_CONFIG_SCALRY_KEY_LOG);
        String logNewRelicConfigString = remoteConfig.getString(REMOTE_CONFIG_NEW_RELIC_KEY_LOG);
        TimberReportingTree timberReportingTree = TimberReportingTree.Companion.getInstance();
        if (!TextUtils.isEmpty(logScalyrConfigString)) {
            DataLogConfig dataLogConfig = new Gson().fromJson(logScalyrConfigString, DataLogConfig.class);
            DataLogConfig dataLogConfigNewRelic = new Gson().fromJson(logNewRelicConfigString, DataLogConfig.class);
            if (dataLogConfig != null && dataLogConfig.isEnabled() && GlobalConfig.VERSION_CODE >= dataLogConfig.getAppVersionMin() && dataLogConfig.getTags() != null) {
                UserSession userSession = new UserSession(context);
                timberReportingTree.setPopulateTagMaps(dataLogConfig.getTags());
                timberReportingTree.setPopulateTagMapsNewRelic(dataLogConfigNewRelic.getTags());
                timberReportingTree.setUserId(userSession.getUserId());
                timberReportingTree.setPartDeviceId(LoggerUtils.INSTANCE.getPartDeviceId(context));
                timberReportingTree.setVersionName(GlobalConfig.RAW_VERSION_NAME);
                timberReportingTree.setVersionCode(GlobalConfig.VERSION_CODE);
                timberReportingTree.setClientLogs(dataLogConfig.getClientLogs());
                timberReportingTree.setQueryLimits(dataLogConfig.getQueryLimits());
            }
        }
    }

    private static List<ScalyrConfig> getScalyrConfigList(Context context) {
        List<ScalyrConfig> scalyrConfigList = new ArrayList<>();
        for (int i = 0; i < PRIORITY_LENGTH; i++) {
            scalyrConfigList.add(getScalyrConfig(context, i + 1));
        }
        return scalyrConfigList;
    }

    private static ScalyrConfig getScalyrConfig(Context context, int priority) {
        String session = LoggerUtils.INSTANCE.getLogSession(context);
        String serverHost = String.format("android-seller-app-p%s", priority);
        String parser = "android-parser";
        return new ScalyrConfig(Keys.getAUTH_SCALYR_API_KEY(), session, serverHost, parser, context.getPackageName(),
                String.valueOf(context.getPackageManager().getInstallerPackageName(context.getPackageName())),
                GlobalConfig.DEBUG, priority);
    }
}