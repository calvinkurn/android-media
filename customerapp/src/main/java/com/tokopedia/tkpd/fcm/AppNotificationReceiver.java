package com.tokopedia.tkpd.fcm;

import android.app.Application;
import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.INotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.utils.GCMUtils;
import com.tokopedia.tkpd.ConsumerMainApplication;

import java.util.Map;

import rx.Observable;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * Created by alvarisi on 1/17/17.
 */

public class AppNotificationReceiver implements IAppNotificationReceiver {
    private AppNotificationReceiverUIBackground mAppNotificationReceiverUIBackground;
    private INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;

    public AppNotificationReceiver() {

    }

    public void init(Application application) {
        mAppNotificationReceiverUIBackground = new AppNotificationReceiverUIBackground(application);
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
    }

    public void onNotificationReceived(String from, Bundle bundle) {
        CommonUtils.dumper("FCM messaging " + bundle.toString());
        if(bundle.containsKey(Constants.ARG_NOTIFICATION_APPLINK)) {
            bundle.putString("origin", "moengage");
        }
        mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(Observable.just(bundle));
        mNotificationAnalyticsReceiver.onNotificationReceived(Observable.just(bundle));
    }
}
