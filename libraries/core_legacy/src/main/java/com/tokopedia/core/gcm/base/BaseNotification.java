package com.tokopedia.core.gcm.base;

import android.content.Context;
import android.media.RingtoneManager;
import android.os.Bundle;

import com.tokopedia.core.gcm.BuildAndShowNotification;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.NotificationConfiguration;
import com.tokopedia.core.gcm.Visitable;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * @author  by alvarisi on 1/12/17.
 */

public abstract class BaseNotification implements Visitable {
    protected final Context mContext;
    private final FCMCacheManager mFCMCacheManager;
    protected NotificationConfiguration configuration;
    protected NotificationPass mNotificationPass;
    protected BuildAndShowNotification mBuildAndShowNotification;

    protected BaseNotification(Context context) {
        mContext = context;
        mNotificationPass = new NotificationPass();
        mBuildAndShowNotification = new BuildAndShowNotification(mContext);
        mFCMCacheManager = new FCMCacheManager(mContext);
        configuration = new NotificationConfiguration();

        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "BaseNotification");
        ServerLogger.log(Priority.P2, "PUSH_NOTIF_UNUSED", messageMap);
    }

    @Override
    public void proccessReceivedNotification(Bundle incomingMessage) {
        configureNotificationData(incomingMessage);
        buildDefaultConfiguration();
        showNotification(incomingMessage);
    }

    protected void buildDefaultConfiguration() {
        configuration.setBell(mFCMCacheManager.isAllowBell());
        configuration.setVibrate(mFCMCacheManager.isVibrate());
        configuration.setSoundUri(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
    }

    protected void showNotification(Bundle incomingMessage){
        mBuildAndShowNotification.buildAndShowNotification(mNotificationPass, incomingMessage, configuration);
    }

    protected abstract void configureNotificationData(Bundle data);
}
