package com.tokopedia.sellerapp.fcm;

import android.app.Application;
import android.os.Bundle;

import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.INotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.utils.ActivitiesLifecycleCallbacks;
import com.tokopedia.core.gcm.utils.GCMUtils;

import rx.Observable;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * Created by alvarisi on 1/18/17.
 */

public enum  AppNotificationReceiver {
    Notifications;
    AppNotificationReceiverUIBackground mAppNotificationReceiverUIBackground;
    private FCMCacheManager cacheManager;
    INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;
    ActivitiesLifecycleCallbacks mActivitiesLifecycleCallbacks;

    void init(Application application){
        mAppNotificationReceiverUIBackground = new AppNotificationReceiverUIBackground(application);
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
        mActivitiesLifecycleCallbacks = new ActivitiesLifecycleCallbacks(application);
        cacheManager = new FCMCacheManager(application.getBaseContext());
    }

    public void onNotificationReceived(String from, Bundle data){
        if (isAllowedNotification(data)){
            cacheManager.setCache();
            mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(Observable.just(data));
        }
        mNotificationAnalyticsReceiver.onNotificationReceived(Observable.just(data));
    }

    private boolean isAllowedNotification(Bundle data) {
        return GCMUtils.isValidForSellerApp(GCMUtils.getCode(data))
                && cacheManager.isAllowToHandleNotif(data)
                && cacheManager.checkLocalNotificationAppSettings(
                Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))
        );
    }
}
