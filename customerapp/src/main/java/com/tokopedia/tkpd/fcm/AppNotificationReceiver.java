package com.tokopedia.tkpd.fcm;

import android.app.Application;
import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;
import com.moengage.push.PushManager;
import com.moengage.pushbase.push.MoEngageNotificationUtils;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.INotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.utils.GCMUtils;
import com.tokopedia.tkpd.ConsumerMainApplication;

import java.util.Map;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;

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

    @Override
    public void onMoengageNotificationReceived(RemoteMessage message) {
        CommonUtils.dumper("FCM messaging moengage " + message.getData().toString());
        if(message.getData().containsKey(Constants.ARG_NOTIFICATION_APPLINK)){
            Map<String, String> appLinkData = message.getData();
            appLinkData.put(Constants.KEY_ORIGIN,Constants.ARG_NOTIFICATION_APPLINK_PROMO_LABEL);
            mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(Observable.just(GCMUtils.convertMap(appLinkData)));
        }else {
            PushManager.getInstance().getPushHandler().handlePushPayload(ConsumerMainApplication.getAppContext(), message.getData());
        }
    }

    public void onNotificationReceived(String from, Bundle bundle) {
        CommonUtils.dumper("FCM messaging " + bundle.toString());
        if(bundle.containsKey(Constants.ARG_NOTIFICATION_ISPROMO)) {
            bundle.putString(Constants.KEY_ORIGIN,Constants.ARG_NOTIFICATION_APPLINK_PROMO_LABEL);
        }
        mAppNotificationReceiverUIBackground.notifyReceiverBackgroundMessage(Observable.just(bundle));
        mNotificationAnalyticsReceiver.onNotificationReceived(Observable.just(bundle));
    }

}
