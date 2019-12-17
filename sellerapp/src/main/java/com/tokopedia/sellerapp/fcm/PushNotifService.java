package com.tokopedia.sellerapp.fcm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.RemoteMessage;
import com.moengage.pushbase.push.MoEngageNotificationUtils;
import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.base.BaseNotificationMessagingService;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.seller.fcm.constant.PushNotificationConstant;

import java.util.Map;

import io.hansel.hanselsdk.Hansel;

public class PushNotifService extends BaseNotificationMessagingService {
    private static IAppNotificationReceiver appNotificationReceiver;
    private SharedPreferences sharedPreferences;
    private Context mContext;;
    private SessionHandler sessionHandler;
    private GCMHandler gcmHandler;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mContext = getApplicationContext();
        sessionHandler = RouterUtils.getRouterFromContext(mContext).legacySessionHandler();
        gcmHandler = new GCMHandler(mContext);
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);

        Bundle data = convertMap(remoteMessage);
        CommonUtils.dumper("FCM " + data.toString());

        if (appNotificationReceiver == null) {
            appNotificationReceiver = createInstance(mContext);
            appNotificationReceiver.init(getApplication());
        }

        if (Hansel.isPushFromHansel(data) && !GlobalConfig.isSellerApp()) {
            Hansel.handlePushPayload(this, data);
        }else if (MoEngageNotificationUtils.isFromMoEngagePlatform(remoteMessage.getData()) && showPromoNotification()) {
            appNotificationReceiver.onMoengageNotificationReceived(remoteMessage);
        }else if (appNotificationReceiver.isFromCMNotificationPlatform(remoteMessage.getData())) {
            appNotificationReceiver.onCampaignManagementNotificationReceived(remoteMessage);
        } else {
            AnalyticsLog.logNotification(mContext, sessionHandler, remoteMessage.getFrom(), data.getString(Constants.ARG_NOTIFICATION_CODE, ""));
            appNotificationReceiver.onNotificationReceived(remoteMessage.getFrom(), data);
        }

        //Send intent from this service only
        sendPushNotificationIntent();
    }

    private boolean showPromoNotification() {
        if(sharedPreferences == null) sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        return sharedPreferences.getBoolean(Constants.Settings.NOTIFICATION_PROMO, true);
    }

    /**
     * Send dataless intent for each incoming push notification
     */
    private void sendPushNotificationIntent() {
        Intent intent = new Intent(PushNotificationConstant.ACTION_PUSH_NOTIFICATION);
        if (localBroadcastManager != null)
            localBroadcastManager.sendBroadcast(intent);
    }

    public static IAppNotificationReceiver createInstance(Context context) {
        if (GlobalConfig.isSellerApp()) {
            return TkpdCoreRouter.getAppNotificationReceiver(context);
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
                public void onCampaignManagementNotificationReceived(RemoteMessage message) {

                }

                @Override
                public boolean isFromCMNotificationPlatform(Map<String, String> extra) {
                    return false;
                }
            };
        } else {
            return HomeRouter.getAppNotificationReceiver();
        }
    }
}
