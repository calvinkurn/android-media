package com.tokopedia.tkpd.fcm;

import android.app.Application;
import android.os.Bundle;

import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.INotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;

import rx.Observable;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * Created by alvarisi on 1/17/17.
 */

public enum AppNotificationReceiver {
    Notifications;
    public static String TAG = "PUSHNOTIFTAG";
    private AppNotificationReceiverUIBackground mAppNotificationReceiverUIBackground;
    private FCMCacheManager cacheManager;
    private INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;

    void init(Application application) {
        mAppNotificationReceiverUIBackground = new AppNotificationReceiverUIBackground(application);
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
        cacheManager = new FCMCacheManager(application.getBaseContext());
    }

    public void onNotificationReceived(String from, Bundle bundle) {
        if (isAllowedNotification(bundle)) {
            cacheManager.setCache();
            mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(Observable.just(bundle));
        }
        mNotificationAnalyticsReceiver.onNotificationReceived(Observable.just(bundle));
    }

    private boolean isAllowedNotification(Bundle data) {
        return cacheManager.isAllowToHandleNotif(data)
                && cacheManager.checkLocalNotificationAppSettings(
                Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)
                )
        );
    }
}
