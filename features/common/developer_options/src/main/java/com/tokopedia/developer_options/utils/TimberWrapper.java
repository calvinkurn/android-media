package com.tokopedia.developer_options.utils;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.logger.utils.DataLogConfig;
import com.tokopedia.logger.utils.LoggerUtils;
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

    public static final String REMOTE_CONFIG_KEY_LOG_CUSTOMER_APP = "android_customer_app_log_config";
    public static final String REMOTE_CONFIG_KEY_LOG_SELLER_APP = "android_seller_app_log_config";

    public static void initByRemoteConfig(@NonNull Context context, String remoteConfigKey){
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        Timber.uprootAll();
        String logConfigString = remoteConfig.getString(remoteConfigKey);
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