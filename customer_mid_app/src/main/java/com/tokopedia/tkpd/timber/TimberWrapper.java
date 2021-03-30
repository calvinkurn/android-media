package com.tokopedia.tkpd.timber;

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
import com.tokopedia.logger.utils.LoggerReporting;
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

    private static final String REMOTE_CONFIG_SCALRY_KEY_LOG = "android_customerapp_log_config_scalyr";
    private static final String REMOTE_CONFIG_NEW_RELIC_KEY_LOG = "android_customerapp_log_config_new_relic";

    public static void init(Application application) {
        LogManager.init(application);
        initConfig(application);
    }

    public static void initConfig(@NonNull Context context) {
        initByRemoteConfig(context, new FirebaseRemoteConfigImpl(context));
    }

    public static void initByRemoteConfig(@NonNull Context context, @NonNull RemoteConfig remoteConfig) {
        plantTimberReleaseTree(context, remoteConfig);
    }

    private static void plantTimberReleaseTree(Context context, @NonNull RemoteConfig remoteConfig) {
        try {
            String logScalyrConfigString = remoteConfig.getString(REMOTE_CONFIG_SCALRY_KEY_LOG);
            String logNewRelicConfigString = remoteConfig.getString(REMOTE_CONFIG_NEW_RELIC_KEY_LOG);
            LoggerReporting loggerReporting = LoggerReporting.Companion.getInstance();
            if (!TextUtils.isEmpty(logScalyrConfigString)) {
                DataLogConfig dataLogConfigScalyr = new Gson().fromJson(logScalyrConfigString, DataLogConfig.class);
                if (dataLogConfigScalyr != null && dataLogConfigScalyr.isEnabled() && GlobalConfig.VERSION_CODE >= dataLogConfigScalyr.getAppVersionMin()
                        && dataLogConfigScalyr.getTags() != null) {
                    UserSession userSession = new UserSession(context);
                    loggerReporting.setPopulateTagMaps(dataLogConfigScalyr.getTags());
                    loggerReporting.setUserId(userSession.getUserId());
                    loggerReporting.setPartDeviceId(LoggerUtils.INSTANCE.getPartDeviceId(context));
                    loggerReporting.setVersionName(GlobalConfig.RAW_VERSION_NAME);
                    loggerReporting.setVersionCode(GlobalConfig.VERSION_CODE);
                    loggerReporting.setClientLogs(dataLogConfigScalyr.getClientLogs());
                    loggerReporting.setQueryLimits(dataLogConfigScalyr.getQueryLimits());
                }
            }
            if (!TextUtils.isEmpty(logNewRelicConfigString)) {
                DataLogConfig dataLogConfigNewRelic = new Gson().fromJson(logNewRelicConfigString, DataLogConfig.class);
                if (dataLogConfigNewRelic.getTags() != null) {
                    loggerReporting.setPopulateTagMapsNewRelic(dataLogConfigNewRelic.getTags());
                }
            }
        } catch (Throwable throwable) {
            // do nothing
        }
    }
}
