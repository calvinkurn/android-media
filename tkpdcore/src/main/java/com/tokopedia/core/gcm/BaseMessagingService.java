package com.tokopedia.core.gcm;

import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;
import com.moengage.pushbase.push.MoEngageNotificationUtils;
import com.tkpd.library.utils.AnalyticsLog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.gcm.base.BaseNotificationMessagingService;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;

/**
 * Created by alvarisi on 3/17/17.
 */

public class BaseMessagingService extends BaseNotificationMessagingService {
    private static IAppNotificationReceiver appNotificationReceiver;

    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Bundle data = convertMap(remoteMessage);
        CommonUtils.dumper("FCM " + data.toString());

        if (appNotificationReceiver == null) {
            appNotificationReceiver = createInstance();
            appNotificationReceiver.init(getApplication());
        }

        if (MoEngageNotificationUtils.isFromMoEngagePlatform(remoteMessage.getData())) {
            appNotificationReceiver.onMoengageNotificationReceived(remoteMessage);
        } else {
            AnalyticsLog.logNotification(remoteMessage.getFrom(), data.getString(Constants.ARG_NOTIFICATION_CODE, ""));
            appNotificationReceiver.onNotificationReceived(remoteMessage.getFrom(), data);
        }
    }

    public static IAppNotificationReceiver createInstance() {
        if (GlobalConfig.isSellerApp()) {
            return SellerAppRouter.getAppNotificationReceiver();
        } else {
            return HomeRouter.getAppNotificationReceiver();
        }
    }
}
