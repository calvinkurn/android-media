package com.tokopedia.sellerapp.fcm;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;
import com.moengage.push.PushManager;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.INotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.utils.ActivitiesLifecycleCallbacks;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.sellerapp.SellerMainApplication;

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
            PushNotification.notify(mContext, data);
        } else {
            mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(data);
        }
        mNotificationAnalyticsReceiver.onNotificationReceived(Observable.just(data));
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

}
