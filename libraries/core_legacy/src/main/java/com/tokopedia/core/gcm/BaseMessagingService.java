package com.tokopedia.core.gcm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.firebase.messaging.RemoteMessage;
import com.moengage.pushbase.push.MoEngageNotificationUtils;
import com.tkpd.library.utils.legacy.AnalyticsLog;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.base.BaseNotificationMessagingService;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.core.router.home.HomeRouter;

import java.util.Map;

import io.hansel.hanselsdk.Hansel;
import timber.log.Timber;

/**
 * Created by alvarisi on 3/17/17.
 */

public class BaseMessagingService extends BaseNotificationMessagingService {
    private static IAppNotificationReceiver appNotificationReceiver;
    private SharedPreferences sharedPreferences;
    private Context mContext;;
    private SessionHandler sessionHandler;
    private GCMHandler gcmHandler;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        mContext = getApplicationContext();
        sessionHandler = RouterUtils.getRouterFromContext(mContext).legacySessionHandler();
        gcmHandler = new GCMHandler(mContext);

        Bundle data = convertMap(remoteMessage);
        Timber.d("FCM " + data.toString());

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
    }

    private boolean showPromoNotification() {
        if(sharedPreferences == null) sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        return sharedPreferences.getBoolean(Constants.Settings.NOTIFICATION_PROMO, true);
    }

    public static IAppNotificationReceiver createInstance(Context context) {
        if (GlobalConfig.isSellerApp()) {
            return TkpdCoreRouter.getAppNotificationReceiver(context);
        } else {
            return HomeRouter.getAppNotificationReceiver();
        }
    }
}
