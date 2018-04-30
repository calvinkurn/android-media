package com.tokopedia.tkpd.fcm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.RemoteMessage;
import com.moengage.push.PushManager;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.utils.ActivitiesLifecycleCallbacks;
import com.tokopedia.inbox.inboxchat.ChatNotifInterface;
import com.tokopedia.pushnotif.ApplinkNotificationHelper;
import com.tokopedia.pushnotif.Constant;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;
import com.tokopedia.tkpd.ConsumerMainApplication;

import com.tokopedia.core.gcm.base.IAppNotificationReceiver;

import rx.Observable;

/**
 * Created by alvarisi on 1/17/17.
 */

public class AppNotificationReceiver implements IAppNotificationReceiver {
    private AppNotificationReceiverUIBackground mAppNotificationReceiverUIBackground;
    private Context mContext;
    private ActivitiesLifecycleCallbacks mActivitiesLifecycleCallbacks;

    public AppNotificationReceiver() {

    }

    public void init(Application application) {
        if (mAppNotificationReceiverUIBackground == null)
            mAppNotificationReceiverUIBackground = new AppNotificationReceiverUIBackground(application);

        mContext = application.getApplicationContext();
        mActivitiesLifecycleCallbacks = new ActivitiesLifecycleCallbacks(application);
    }

    @Override
    public void onMoengageNotificationReceived(RemoteMessage message) {
        PushManager.getInstance().getPushHandler().handlePushPayload(ConsumerMainApplication.getAppContext(), message.getData());
    }

    public void onNotificationReceived(String from, Bundle bundle) {
        if (bundle.containsKey(Constants.ARG_NOTIFICATION_ISPROMO)) {
            bundle.putString(Constants.KEY_ORIGIN, Constants.ARG_NOTIFICATION_APPLINK_PROMO_LABEL);
        }

        if (isApplinkNotification(bundle)) {
            PushNotification.notify(mContext, bundle, getCurrentActivity());
            extraAction(bundle);
        } else {
            mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(bundle);
        }
    }

    private int getCurrentActivity(){
        if(mActivitiesLifecycleCallbacks instanceof ChatNotifInterface) {
            return Constant.NotificationId.CHAT;
        }
        return 0;
    }

    private void extraAction(Bundle data) {
        if (canBroadcastPointReceived(
                data.getString(Constants.ARG_NOTIFICATION_CODE, "0"))) {
            broadcastPointReceived(data);
        }
    }

    private void broadcastPointReceived(Bundle data) {
        Intent loyaltyGroupChat = new Intent(com.tokopedia.abstraction.constant
                .TkpdState.LOYALTY_GROUP_CHAT);
        loyaltyGroupChat.putExtras(data);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(loyaltyGroupChat);
    }

    private boolean canBroadcastPointReceived(String tkpCode){
        final String GROUP_CHAT_BROADCAST_TKP_CODE = "140";
        final String GROUP_CHAT_BROADCAST_TKP_CODE_GENERAL = "1400";

        return tkpCode.startsWith(GROUP_CHAT_BROADCAST_TKP_CODE)
                && !tkpCode.equals(GROUP_CHAT_BROADCAST_TKP_CODE_GENERAL);
    }

    private boolean isApplinkNotification(Bundle data) {
        return !data.getString(Constants.ARG_NOTIFICATION_APPLINK, "").equals("");
    }

}
