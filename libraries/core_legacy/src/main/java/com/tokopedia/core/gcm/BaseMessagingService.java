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
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.base.BaseNotificationMessagingService;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.intentservices.PushNotificationIntentService;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Map;

import io.hansel.hanselsdk.Hansel;
import timber.log.Timber;

/**
 * Created by alvarisi on 3/17/17.
 */

public class BaseMessagingService extends BaseNotificationMessagingService {
    private static IAppNotificationReceiver appNotificationReceiver;
    private SharedPreferences sharedPreferences;
    private Context mContext;;
    private SessionHandler sessionHandler;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mContext = getApplicationContext();
        sessionHandler = RouterUtils.getRouterFromContext(mContext).legacySessionHandler();
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);

        Bundle data = convertMap(remoteMessage);
        Timber.d("FCM " + data.toString());

        if (appNotificationReceiver == null) {
            appNotificationReceiver = createInstance(mContext);
            appNotificationReceiver.init(getApplication());
        }

        if (Hansel.isPushFromHansel(data) && !GlobalConfig.isSellerApp()) {
            Hansel.handlePushPayload(this, data);
            Timber.w("P1#MESSAGING_SERVICE#HanselPush;from='%s';data='%s'", remoteMessage.getFrom(), data.toString());
        }else if (MoEngageNotificationUtils.isFromMoEngagePlatform(remoteMessage.getData()) && showPromoNotification()) {
            appNotificationReceiver.onMoengageNotificationReceived(remoteMessage);
            Timber.w("P1#MESSAGING_SERVICE#MoengageNotification;from='%s';data='%s'", remoteMessage.getFrom(), data.toString());
        }else if (appNotificationReceiver.isFromCMNotificationPlatform(remoteMessage.getData())) {
            appNotificationReceiver.onCampaignManagementNotificationReceived(remoteMessage);
            Timber.w("P1#MESSAGING_SERVICE#CampaignManagementNotification;from='%s';data='%s'", remoteMessage.getFrom(), data.toString());
        } else {
            AnalyticsLog.logNotification(mContext, sessionHandler, remoteMessage.getFrom(), data.getString(Constants.ARG_NOTIFICATION_CODE, ""));
            appNotificationReceiver.onNotificationReceived(remoteMessage.getFrom(), data);
            logTokopediaNotification(remoteMessage);
        }
        logOnMessageReceived(remoteMessage, data);

        if (com.tokopedia.config.GlobalConfig.isSellerApp()) {
            sendPushNotificationIntent();
        }
    }

    private void logOnMessageReceived(RemoteMessage remoteMessage, Bundle data) {
        try {
//            String whiteListedUsers = FirebaseRemoteConfig.getInstance().getString(RemoteConfigKey.WHITELIST_USER_LOG_NOTIFICATION);
            String userId = sessionHandler.getUserId();
//            if (!userId.isEmpty() && whiteListedUsers.contains(userId)) {
            if (!userId.isEmpty()) {
                executeLogOnMessageReceived(remoteMessage, data);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void executeLogOnMessageReceived(RemoteMessage remoteMessage, Bundle data) {
//        if (!BuildConfig.DEBUG) {
//            String notificationCode = getNotificationCode(remoteMessage);
            StringBuilder logMessage = new StringBuilder("onMessageReceived FirebaseMessagingService \n");
            for (String key : data.keySet()) {
                logMessage.append(key);
                logMessage.append(": ");
                logMessage.append(data.get(key));
                logMessage.append(", \n");
            }
//            String errorMessage = "onMessageReceived FirebaseMessagingService, " +
//                    "userId: " + sessionHandler.getUserId() + ", " +
//                    "userEmail: " + sessionHandler.getEmail() + ", " +
//                    "deviceId: " + sessionHandler.getDeviceId() + ", " +
//                    "notificationId: " + remoteMessage.getFrom() + ", " +
//                    "notificationCode: " + notificationCode;
            Crashlytics.logException(new Exception(logMessage.toString()));
//        }
    }

    private String getNotificationCode(RemoteMessage remoteMessage) {
        Map<String, String> payload = remoteMessage.getData();
        if (payload.containsKey(Constants.ARG_NOTIFICATION_CODE)) {
            return payload.get(Constants.ARG_NOTIFICATION_CODE);
        }
        return "";
    }

    private boolean showPromoNotification() {
        if(sharedPreferences == null) sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

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
