package com.tokopedia.core.gcm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.moengage.pushbase.push.MoEngageNotificationUtils;
import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.gcm.base.BaseNotificationMessagingService;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.intentservices.PushNotificationIntentService;
import com.tokopedia.fcmcommon.FirebaseMessagingManagerImpl;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import io.hansel.hanselsdk.Hansel;
import timber.log.Timber;

/**
 * Created by alvarisi on 3/17/17.
 */

public class BaseMessagingService extends BaseNotificationMessagingService {
    private static IAppNotificationReceiver appNotificationReceiver;
    private SharedPreferences sharedPreferences;
    private Context mContext;
    private LocalBroadcastManager localBroadcastManager;
    private UserSessionInterface userSession;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mContext = getApplicationContext();
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        userSession = new UserSession(this);

        Bundle data = convertMap(remoteMessage);
        Timber.d("FCM " + data.toString());

        if (appNotificationReceiver == null) {
            appNotificationReceiver = createInstance(mContext);
            appNotificationReceiver.init(getApplication());
        }

        if (Hansel.isPushFromHansel(data) && !GlobalConfig.isSellerApp()) {
            Hansel.handlePushPayload(this, data);
            Timber.w("P1#MESSAGING_SERVICE#HanselPush;from='%s';data='%s'", remoteMessage.getFrom(), data.toString());
        } else if (MoEngageNotificationUtils.isFromMoEngagePlatform(remoteMessage.getData()) && showPromoNotification()) {
            appNotificationReceiver.onMoengageNotificationReceived(remoteMessage);
            Timber.w("P1#MESSAGING_SERVICE#MoengageNotification;from='%s';data='%s'", remoteMessage.getFrom(), data.toString());
        } else if (appNotificationReceiver.isFromCMNotificationPlatform(remoteMessage.getData())) {
            appNotificationReceiver.onCampaignManagementNotificationReceived(remoteMessage);
            Timber.w("P1#MESSAGING_SERVICE#CampaignManagementNotification;from='%s';data='%s'", remoteMessage.getFrom(), data.toString());
        } else {
            AnalyticsLog.logNotification(mContext, userSession.getUserId(), remoteMessage.getFrom(), data.getString(Constants.ARG_NOTIFICATION_CODE, ""));
            appNotificationReceiver.onNotificationReceived(remoteMessage.getFrom(), data);
            logTokopediaNotification(remoteMessage);
        }
        logOnMessageReceived(data);

        if (com.tokopedia.config.GlobalConfig.isSellerApp()) {
            sendPushNotificationIntent();
        }
    }

    private void logOnMessageReceived(Bundle data) {
        try {
            String whiteListedUsers = FirebaseRemoteConfig.getInstance().getString(RemoteConfigKey.WHITELIST_USER_LOG_NOTIFICATION);
            String userId = userSession.getUserId();
            if (!userId.isEmpty() && whiteListedUsers.contains(userId)) {
                executeLogOnMessageReceived(data);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void executeLogOnMessageReceived(Bundle data) {
        if (!BuildConfig.DEBUG) {
            String logMessage = generateLogMessage(data);
            Crashlytics.logException(new Exception(logMessage));
            Timber.w(
                    "P2#LOG_PUSH_NOTIF#'%s';data='%s'",
                    "BaseMessagingService::onMessageReceived",
                    logMessage
            );
        }
    }

    private String generateLogMessage(Bundle data) {
        StringBuilder logMessage = new StringBuilder("BaseMessagingService::onMessageReceived \n");
        String fcmToken = FirebaseMessagingManagerImpl.getFcmTokenFromPref(this);
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

    private boolean showPromoNotification() {
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        return sharedPreferences.getBoolean(Constants.Settings.NOTIFICATION_PROMO, true);
    }

    /**
     * Send dataless intent for each incoming push notification
     */
    private void sendPushNotificationIntent() {
        Intent intent = new Intent(PushNotificationIntentService.UPDATE_NOTIFICATION_DATA);
        if (localBroadcastManager != null)
            localBroadcastManager.sendBroadcast(intent);
    }

    private void logTokopediaNotification(RemoteMessage remoteMessage) {
        // Remove sensitive summary content for logging
        Bundle bundleTemp = convertMap(remoteMessage);
        bundleTemp.remove("summary");
        bundleTemp.remove("desc");
        Timber.w("P1#MESSAGING_SERVICE#TokopediaNotification;from='%s';data='%s'", remoteMessage.getFrom(), bundleTemp.toString());
    }

    public static IAppNotificationReceiver createInstance(Context context) {
        return ((TkpdCoreRouter) context.getApplicationContext()).getAppNotificationReceiver();
    }
}
