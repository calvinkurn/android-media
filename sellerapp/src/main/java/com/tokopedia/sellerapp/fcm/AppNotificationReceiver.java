package com.tokopedia.sellerapp.fcm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.moengage.push.PushManager;
import com.tokopedia.abstraction.constant.TkpdState;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.INotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.utils.ActivitiesLifecycleCallbacks;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.topchat.chatlist.view.ChatNotifInterface;
import com.tokopedia.pushnotif.ApplinkNotificationHelper;
import com.tokopedia.pushnotif.Constant;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;
import com.tokopedia.sellerapp.SellerMainApplication;
import com.tokopedia.topchat.chatlist.view.ChatNotifInterface;
import com.tokopedia.user.session.UserSession;

import java.util.Map;

import rx.Observable;
import timber.log.Timber;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * Created by alvarisi on 1/18/17.
 */

public class AppNotificationReceiver  implements IAppNotificationReceiver {
    private AppNotificationReceiverUIBackground mAppNotificationReceiverUIBackground;
    private FCMCacheManager cacheManager;
    private INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;
    private ActivitiesLifecycleCallbacks mActivitiesLifecycleCallbacks;
    private UserSession userSession;

    private Context mContext;

    public AppNotificationReceiver(UserSession session) {
        userSession = session;
    }

    public void init(Application application){
        mAppNotificationReceiverUIBackground = new AppNotificationReceiverUIBackground(application);
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
        mActivitiesLifecycleCallbacks = new ActivitiesLifecycleCallbacks(application);
        cacheManager = new FCMCacheManager(application.getBaseContext());

        mContext = application.getApplicationContext();
    }

    public void onNotificationReceived(String from, Bundle data){
        Timber.d("onNotificationReceived");
        if (isApplinkNotification(data)) {
            logEvent(data, "isApplinkNotification(data) == true");
            if (!isInExcludedActivity(data)) {
                logEvent(data, "!isInExcludedActivity(data) == true");
                PushNotification.notify(mContext, data);
            } else {
                logEvent(data, "!isInExcludedActivity(data) == false");
            }
            extraAction(data);
        } else {
            logEvent(data, "isApplinkNotification(data) == false");
            mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(data);
        }
        mNotificationAnalyticsReceiver.onNotificationReceived(Observable.just(data));
    }

    private void logEvent(Bundle data, String message) {
        try {
//            String whiteListedUsers = FirebaseRemoteConfig.getInstance().getString(RemoteConfigKey.WHITELIST_USER_LOG_NOTIFICATION);
            String userId = userSession.getUserId();
//            if (!userId.isEmpty() && whiteListedUsers.contains(userId)) {
            if (!userId.isEmpty()) {
                executeCrashlyticLog(data,  message);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void executeCrashlyticLog(Bundle data, String message) {
//        if (!BuildConfig.DEBUG) {
            StringBuilder logMessage = new StringBuilder(message + "\n");
            for (String key : data.keySet()) {
                logMessage.append(key);
                logMessage.append(": ");
                logMessage.append(data.get(key));
                logMessage.append(", \n");
            }
            Crashlytics.logException(new Exception(logMessage.toString()));
//        }
    }

    private boolean isInExcludedActivity(Bundle data) {
        ApplinkNotificationModel applinkNotificationModel = ApplinkNotificationHelper.convertToApplinkModel(data);
        int notificationId = ApplinkNotificationHelper.generateNotifictionId(applinkNotificationModel.getApplinks());
        return notificationId == getCurrentNotifIdByActivity();
    }

    private int getCurrentNotifIdByActivity(){
        if(mActivitiesLifecycleCallbacks.getLiveActivityOrNull() == null){
            return 0;
        }
        if(mActivitiesLifecycleCallbacks.getLiveActivityOrNull() instanceof ChatNotifInterface) {
            return Constant.NotificationId.CHAT;
        }
        return 0;
    }

    private void extraAction(Bundle data) {
        String code = data.getString(Constants.ARG_NOTIFICATION_CODE, "0");
        if(canBroadcastChat(code)){
            broadcastNotifTopChat(data);
        }
    }


    private void broadcastNotifTopChat(Bundle data) {
        Intent loyaltyGroupChat = new Intent(TkpdState.TOPCHAT);
        loyaltyGroupChat.putExtras(data);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(loyaltyGroupChat);
    }

    private boolean canBroadcastChat(String tkpCode){
        final String CHAT_BROADCAST_TKP_CODE = "111";
        return tkpCode.startsWith(CHAT_BROADCAST_TKP_CODE);
    }

    @Override
    public void onMoengageNotificationReceived(RemoteMessage message) {
        PushManager.getInstance().getPushHandler().handlePushPayload(SellerMainApplication.getAppContext(), message.getData());
    }

    @Override
    public void onCampaignManagementNotificationReceived(RemoteMessage message) {

    }

    @Override
    public boolean isFromCMNotificationPlatform(Map<String, String> extra) {
        return false;
    }

    private boolean isAllowedNotification(Bundle data) {
        return cacheManager.isAllowToHandleNotif(data)
                && cacheManager.checkLocalNotificationAppSettings(
                Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE, "0"))
        );
    }

    private boolean isApplinkNotification(Bundle data) {
        return !data.getString(Constants.ARG_NOTIFICATION_APPLINK, "").equals("");
    }
}
