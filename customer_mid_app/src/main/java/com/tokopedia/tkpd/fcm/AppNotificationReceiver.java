package com.tokopedia.tkpd.fcm;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.fcmcommon.FirebaseMessagingManagerImpl;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.moengage_wrapper.MoengageInteractor;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.tkpd.app.ConsumerMainApplication;
import com.tokopedia.user.session.UserSession;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by alvarisi on 1/17/17.
 */

public class AppNotificationReceiver implements IAppNotificationReceiver {
    String ARG_NOTIFICATION_ISPROMO = "ispromo";
    private AppNotificationReceiverUIBackground mAppNotificationReceiverUIBackground;
    private Context mContext;
    private UserSession userSession;

    public AppNotificationReceiver() {

    }

    public void init(Application application) {
        if (mAppNotificationReceiverUIBackground == null)
            mAppNotificationReceiverUIBackground = new AppNotificationReceiverUIBackground(application);

        mContext = application.getApplicationContext();
        userSession = new UserSession(mContext);
    }

    @Override
    public void onMoengageNotificationReceived(RemoteMessage message) {
        MoengageInteractor.INSTANCE.handlePushPayload(message.getData());
    }

    @Override
    public void onCampaignManagementNotificationReceived(RemoteMessage message) {
        CMPushNotificationManager.getInstance().handlePushPayload(message);
    }

    @Override
    public boolean isFromCMNotificationPlatform(Map<String, String> extra) {
        return CMPushNotificationManager.getInstance().isFromCMNotificationPlatform(extra);
    }

    public void onNotificationReceived(String from, Bundle bundle) {
        if (bundle.containsKey(ARG_NOTIFICATION_ISPROMO)) {
            bundle.putString(Constants.KEY_ORIGIN, Constants.ARG_NOTIFICATION_APPLINK_PROMO_LABEL);
        }

        if (isApplinkNotification(bundle)) {
            logEvent(bundle, "isApplinkNotification(bundle) == true");
            PushNotification.notify(mContext, bundle);
        } else {
            logEvent(bundle, "isApplinkNotification(bundle) == false");
            mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(bundle);
        }
    }


    private void logEvent(Bundle data, String message) {
        try {
            String whiteListedUsers = FirebaseRemoteConfig.getInstance().getString(RemoteConfigKey.WHITELIST_USER_LOG_NOTIFICATION);
            String userId = userSession.getUserId();
            if (!userId.isEmpty() && whiteListedUsers.contains(userId)) {
                executeCrashlyticLog(data, message);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void executeCrashlyticLog(Bundle data, String message) {
        if (!GlobalConfig.DEBUG) {
            String logMessage = generateLogMessage(data, message);
            FirebaseCrashlytics.getInstance().recordException(new Exception(logMessage));
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "AppNotificationReceiver::onNotificationReceived(String from, Bundle bundle)");
            messageMap.put("data", logMessage);
            ServerLogger.log(Priority.P2, "LOG_PUSH_NOTIF", messageMap);
        }
    }

    private String generateLogMessage(Bundle data, String message) {
        StringBuilder logMessage = new StringBuilder(message + " \n");
        String fcmToken = FirebaseMessagingManagerImpl.getFcmTokenFromPref(mContext);
        addLogLine(logMessage, "fcmToken", fcmToken);
        addLogLine(logMessage, "userId", userSession.getUserId());
        addLogLine(logMessage, "isSellerApp", GlobalConfig.isSellerApp());
        for (String key : data.keySet()) {
            addLogLine(logMessage, key, data.get(key));
        }
        return logMessage.toString();
    }

    private void addLogLine(StringBuilder stringBuilder, String key, Object value) {
        stringBuilder.append(key);
        stringBuilder.append(": ");
        stringBuilder.append(value);
        stringBuilder.append(", \n");
    }

    private boolean isApplinkNotification(Bundle data) {
        return !data.getString(Constants.ARG_NOTIFICATION_APPLINK, "").equals("");
    }

}
