package com.tokopedia.core.gcm;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.google.firebase.messaging.RemoteMessage;
import com.moengage.pushbase.push.MoEngageNotificationUtils;
import com.tkpd.library.utils.AnalyticsLog;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.gcm.base.BaseNotificationMessagingService;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;

import java.util.Map;

/**
 * Created by alvarisi on 3/17/17.
 */

public class BaseMessagingService extends BaseNotificationMessagingService {
    private static final IAppNotificationReceiver appNotificationReceiver = createInstance();

    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Bundle data = convertMap(remoteMessage);
        CommonUtils.dumper("FCM " + data.toString());

        if (appNotificationReceiver != null) {
            appNotificationReceiver.init(getApplication());

            if (MoEngageNotificationUtils.isFromMoEngagePlatform(remoteMessage.getData())) {
                appNotificationReceiver.onMoengageNotificationReceived(remoteMessage);
            } else {
                appNotificationReceiver.onNotificationReceived(remoteMessage.getFrom(), data);
            }
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
