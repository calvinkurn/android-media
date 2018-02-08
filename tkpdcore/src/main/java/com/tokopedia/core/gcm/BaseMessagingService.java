package com.tokopedia.core.gcm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

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
    private static final IAppNotificationReceiver appNotificationReceiver = createInstance();
    private SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Bundle data = convertMap(remoteMessage);
        CommonUtils.dumper("FCM " + data.toString());

        if (appNotificationReceiver != null) {
            appNotificationReceiver.init(getApplication());

            if (MoEngageNotificationUtils.isFromMoEngagePlatform(remoteMessage.getData())) {
                if(showPromoNotification()) {
                    appNotificationReceiver.onMoengageNotificationReceived(remoteMessage);
                }
            } else {
                AnalyticsLog.logNotification(remoteMessage.getFrom(), data.getString(Constants.ARG_NOTIFICATION_CODE, ""));
                appNotificationReceiver.onNotificationReceived(remoteMessage.getFrom(), data);
            }
        }
    }

    private boolean showPromoNotification() {
        if(sharedPreferences == null) sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        return sharedPreferences.getBoolean(Constants.Settings.NOTIFICATION_PROMO, true);
    }

    public static IAppNotificationReceiver createInstance() {
        if (GlobalConfig.isSellerApp()) {
            return SellerAppRouter.getAppNotificationReceiver();
        } else {
            return HomeRouter.getAppNotificationReceiver();
        }
    }
}
