package com.tokopedia.tkpd.fcm;

import android.app.Application;
import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;
import com.moengage.push.PushManager;
import com.moengage.pushbase.push.MoEngageNotificationUtils;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.INotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.utils.GCMUtils;
import com.tokopedia.tkpd.ConsumerMainApplication;

import java.util.Map;

import rx.Observable;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * Created by alvarisi on 1/17/17.
 */

public enum AppNotificationReceiver {
    Notifications;
    private AppNotificationReceiverUIBackground mAppNotificationReceiverUIBackground;
    private INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;

    void init(Application application) {
        mAppNotificationReceiverUIBackground = new AppNotificationReceiverUIBackground(application);
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
    }

    public void onNotificationReceived(RemoteMessage message) {

        if (message != null) {
            Map<String, String> pushPayload = message.getData();
            CommonUtils.dumper("FCM notification received "+message);
            if (MoEngageNotificationUtils.isFromMoEngagePlatform(pushPayload)) {
                CommonUtils.dumper("FCM messaging moengage " + message.getData().toString());
                PushManager.getInstance().getPushHandler().handlePushPayload(ConsumerMainApplication.getAppContext(), pushPayload);
            } else {
                Bundle bundle = GCMUtils.convertMap(message.getData());
                CommonUtils.dumper("FCM messaging "+bundle.toString());
                mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(Observable.just(bundle));
                mNotificationAnalyticsReceiver.onNotificationReceived(Observable.just(bundle));
            }
        }
    }

}
