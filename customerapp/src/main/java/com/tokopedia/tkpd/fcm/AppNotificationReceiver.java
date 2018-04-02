package com.tokopedia.tkpd.fcm;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;
import com.moengage.push.PushManager;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;
import com.tokopedia.pushnotif.PushNotification;
import com.tokopedia.tkpd.ConsumerMainApplication;

import com.tokopedia.core.gcm.base.IAppNotificationReceiver;

import rx.Observable;

/**
 * Created by alvarisi on 1/17/17.
 */

public class AppNotificationReceiver implements IAppNotificationReceiver {
    private AppNotificationReceiverUIBackground mAppNotificationReceiverUIBackground;
    private Context mContext;

    public AppNotificationReceiver() {

    }

    public void init(Application application) {
        if (mAppNotificationReceiverUIBackground == null)
            mAppNotificationReceiverUIBackground = new AppNotificationReceiverUIBackground(application);

        mContext = application.getApplicationContext();
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
            PushNotification.notify(mContext, bundle);
        } else {
            mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(bundle);
        }
    }

    private boolean isApplinkNotification(Bundle data) {
        return !data.getString(Constants.ARG_NOTIFICATION_APPLINK, "").equals("");
    }
}
