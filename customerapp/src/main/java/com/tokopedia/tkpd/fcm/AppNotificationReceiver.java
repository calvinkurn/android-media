package com.tokopedia.tkpd.fcm;

import android.app.Application;
import android.os.Bundle;

import com.tokopedia.core.gcm.INotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.utils.GCMUtils;

import rx.Observable;

/**
 * Created by alvarisi on 1/17/17.
 */

public class AppNotificationReceiver implements IAppNotificationReceiver {
    private AppNotificationReceiverUIBackground mAppNotificationReceiverUIBackground;
    private INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;

    public AppNotificationReceiver() {

    }

    public void init(Application application) {
        System.out.println("Vishal Push init");
        mAppNotificationReceiverUIBackground = new AppNotificationReceiverUIBackground(application);
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
    }

    public void onNotificationReceived(String from, Bundle bundle) {
        System.out.println("Vishal Push recieved =" + GCMUtils.getCode(bundle));
        mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(Observable.just(bundle));
        mNotificationAnalyticsReceiver.onNotificationReceived(Observable.just(bundle));
    }
}
