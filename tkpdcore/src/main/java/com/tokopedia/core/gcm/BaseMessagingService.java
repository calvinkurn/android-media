package com.tokopedia.core.gcm;

import android.app.Application;
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
import com.tokopedia.core.router.posapp.PosAppRouter;
import com.tokopedia.core.util.GlobalConfig;

import java.util.Map;

import io.hansel.hanselsdk.Hansel;

/**
 * Created by alvarisi on 3/17/17.
 */

public class BaseMessagingService extends BaseNotificationMessagingService {
    private static IAppNotificationReceiver appNotificationReceiver;
    private SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Bundle data = convertMap(remoteMessage);
        CommonUtils.dumper("FCM " + data.toString());

        if (appNotificationReceiver == null) {
            appNotificationReceiver = createInstance();
            appNotificationReceiver.init(getApplication());
        }

        if (Hansel.isPushFromHansel(data) && !GlobalConfig.isSellerApp()) {
            Hansel.handlePushPayload(this, data);
        } else if (MoEngageNotificationUtils.isFromMoEngagePlatform(remoteMessage.getData()) && showPromoNotification()) {
            appNotificationReceiver.onMoengageNotificationReceived(remoteMessage);
        } else if (appNotificationReceiver.isFromCMNotificationPlatform(remoteMessage.getData())) {
            appNotificationReceiver.onCampaignManagementNotificationReceived(remoteMessage);
        } else {
            AnalyticsLog.logNotification(remoteMessage.getFrom(), data.getString(Constants.ARG_NOTIFICATION_CODE, ""));
            appNotificationReceiver.onNotificationReceived(remoteMessage.getFrom(), data);
        }
    }

    private boolean showPromoNotification() {
        if(sharedPreferences == null) sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        return sharedPreferences.getBoolean(Constants.Settings.NOTIFICATION_PROMO, true);
    }

    public static IAppNotificationReceiver createInstance() {
        if (GlobalConfig.isSellerApp()) {
            return SellerAppRouter.getAppNotificationReceiver();
        } else if(GlobalConfig.isPosApp()) {
            return new IAppNotificationReceiver() {
                @Override
                public void init(Application application) {
                    // no-op
                }

                @Override
                public void onNotificationReceived(String from, Bundle bundle) {
                    // no-op
                }

                @Override
                public void onMoengageNotificationReceived(RemoteMessage message) {
                    // no-op
                }

                @Override
                public boolean isFromCMNotificationPlatform(Map<String, String> extra) {
                    return false;
                }

                @Override
                public void onCampaignManagementNotificationReceived(RemoteMessage message) {
                    // no-op
                }
            };
        } else {
            return HomeRouter.getAppNotificationReceiver();
        }
    }
}
