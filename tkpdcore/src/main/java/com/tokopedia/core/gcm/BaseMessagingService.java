package com.tokopedia.core.gcm;

import android.os.Bundle;

import com.google.firebase.messaging.RemoteMessage;
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
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Bundle data = convertMap(remoteMessage);
        CommonUtils.dumper(data.toString());
        IAppNotificationReceiver appNotificationReceiver = null;
        if (GlobalConfig.isSellerApp()) {
            appNotificationReceiver = SellerAppRouter.getAppNotificationReceiver();
            if (appNotificationReceiver != null) {
                appNotificationReceiver.init(getApplication());
                appNotificationReceiver.onNotificationReceived(remoteMessage.getFrom(), data);
            }
        } else {
            appNotificationReceiver = HomeRouter.getAppNotificationReceiver();
            if (appNotificationReceiver != null) {
                appNotificationReceiver.init(getApplication());
                appNotificationReceiver.onNotificationReceived(remoteMessage.getFrom(), data);
            }
        }
    }
}
