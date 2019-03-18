package com.tokopedia.sellerapp.fcm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.RemoteMessage;
import com.moengage.push.PushManager;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.constant.TkpdState;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.INotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.utils.ActivitiesLifecycleCallbacks;
import com.tokopedia.topchat.chatlist.view.ChatNotifInterface;
import com.tokopedia.pushnotif.ApplinkNotificationHelper;
import com.tokopedia.pushnotif.Constant;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;
import com.tokopedia.sellerapp.SellerMainApplication;

import java.util.Map;

import rx.Observable;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * Created by alvarisi on 1/18/17.
 */

public class AppNotificationReceiver  implements IAppNotificationReceiver {
    private AppNotificationReceiverUIBackground mAppNotificationReceiverUIBackground;
    private FCMCacheManager cacheManager;
    private INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;
    private ActivitiesLifecycleCallbacks mActivitiesLifecycleCallbacks;

    private Context mContext;

    public AppNotificationReceiver() {
    }

    public void init(Application application){
        mAppNotificationReceiverUIBackground = new AppNotificationReceiverUIBackground(application);
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
        mActivitiesLifecycleCallbacks = new ActivitiesLifecycleCallbacks(application);
        cacheManager = new FCMCacheManager(application.getBaseContext());

        mContext = application.getApplicationContext();
    }

    public void onNotificationReceived(String from, Bundle data){
        CommonUtils.dumper("onNotificationReceived");
        if (isApplinkNotification(data)) {
            if (!isInExcludedActivity(data)) {
                PushNotification.notify(mContext, data);
            }
            extraAction(data);
        } else {
            mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(data);
        }
        mNotificationAnalyticsReceiver.onNotificationReceived(Observable.just(data));
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

    private boolean isAllowedNotification(Bundle data) {
        return cacheManager.isAllowToHandleNotif(data)
                && cacheManager.checkLocalNotificationAppSettings(
                Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE, "0"))
        );
    }

    private boolean isApplinkNotification(Bundle data) {
        return !data.getString(Constants.ARG_NOTIFICATION_APPLINK, "").equals("");
    }

    @Override
    public void onCampaignManagementNotificationReceived(RemoteMessage message) {

    }

    @Override
    public boolean isFromCMNotificationPlatform(Map<String, String> extra) {
        return false;
    }
}
