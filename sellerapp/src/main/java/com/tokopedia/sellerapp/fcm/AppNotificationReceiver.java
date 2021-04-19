package com.tokopedia.sellerapp.fcm;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.INotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.utils.ActivitiesLifecycleCallbacks;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.moengage_wrapper.MoengageInteractor;
import com.tokopedia.notifications.CMPushNotificationManager;
import com.tokopedia.fcmcommon.FirebaseMessagingManagerImpl;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.sellerapp.SellerMainApplication;
import com.tokopedia.user.session.UserSession;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import timber.log.Timber;

import static com.tkpd.remoteresourcerequest.task.ResourceDownloadManager.MANAGER_TAG;

/**
 * Created by alvarisi on 1/18/17.
 */

public class AppNotificationReceiver  implements IAppNotificationReceiver {
    private AppNotificationReceiverUIBackground mAppNotificationReceiverUIBackground;
    private INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;
    private UserSession userSession;

    private Context mContext;

    public AppNotificationReceiver() {

    }

    public void init(Application application){
        mAppNotificationReceiverUIBackground = new AppNotificationReceiverUIBackground(application);
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
        mContext = application.getApplicationContext();
        userSession = new UserSession(mContext);
    }

    public void onNotificationReceived(String from, Bundle data){
        if (isApplinkNotification(data)) {
            logEvent(data, "isApplinkNotification(data) == true");
            PushNotification.notify(mContext, data);
        } else {
            logEvent(data, "isApplinkNotification(data) == false");
            mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(data);
        }
        mNotificationAnalyticsReceiver.onNotificationReceived(Observable.just(data));
    }

    private void logEvent(Bundle data, String message) {
        try {
            String whiteListedUsers = FirebaseRemoteConfig.getInstance().getString(RemoteConfigKey.WHITELIST_USER_LOG_NOTIFICATION);
            String userId = userSession.getUserId();
            if (!userId.isEmpty() && whiteListedUsers.contains(userId)) {
                executeCrashlyticLog(data,  message);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void executeCrashlyticLog(Bundle data, String message) {
        if (!BuildConfig.DEBUG) {
            String logMessage = generateLogMessage(data, message);
            FirebaseCrashlytics.getInstance().recordException(new Exception(logMessage));
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "AppNotificationReceiver::onNotificationReceived(String from, Bundle data)");
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

    private boolean isApplinkNotification(Bundle data) {
        return !data.getString(Constants.ARG_NOTIFICATION_APPLINK, "").equals("");
    }
}
