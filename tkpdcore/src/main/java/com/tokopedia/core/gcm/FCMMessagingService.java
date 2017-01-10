package com.tokopedia.core.gcm;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.Cart;
import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.router.CustomerRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;

import java.io.File;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * @author by hangnadi on 9/7/15.
 *         modified by Hafizh
 *         modified by alvarisi
 */
public class FCMMessagingService extends FirebaseMessagingService {
    AppNotificationReceiverUIBackground mAppNotificationReceiverUIBackground;
    private FCMCacheManager cacheManager;
    INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;
    ActivitiesLifecycleCallbacks mActivitiesLifecycleCallbacks;

    @Override
    public void onMessageReceived(RemoteMessage message) {
        mAppNotificationReceiverUIBackground = new AppNotificationReceiverUIBackground(getApplication());
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
        mActivitiesLifecycleCallbacks = new ActivitiesLifecycleCallbacks(getApplication());
        Bundle data = GCMUtils.convertMap(message.getData());

        CommonUtils.dumper(data.toString());
        cacheManager = new FCMCacheManager(this);
        if (cacheManager.isAllowToHandleNotif(data)) {
            cacheManager.setCache(this);
            if (isDedicatedNotification(data)) {
                handleDedicatedNotification(data);
            } else {
                handlePromotionNotification(data);
            }
        }
        mNotificationAnalyticsReceiver.onNotificationReceived(data);
    }

    private void handlePromotionNotification(Bundle data) {
        switch (GCMUtils.getCode(data)) {
            case TkpdState.GCMServiceState.GCM_HOT_LIST:
//                createNotification(data, BrowseHotDetail.class);
                break;
            case TkpdState.GCMServiceState.GCM_UPDATE_NOTIFICATION:
                mAppNotificationReceiverUIBackground.sendUpdateAppsNotification(data);
                break;
            case TkpdState.GCMServiceState.GCM_PROMO:
                sendMarketingPromoCode(data);
                break;
            case TkpdState.GCMServiceState.GCM_GENERAL:
                sendGeneralNotification(data);
                break;
            case TkpdState.GCMServiceState.GCM_CATEGORY:
//                createNotification(data, BrowseCategory.class);
                break;
            case TkpdState.GCMServiceState.GCM_SHOP:
                mAppNotificationReceiverUIBackground.createNotification(data, ShopInfoActivity.class);
                break;
            case TkpdState.GCMServiceState.GCM_DEEPLINK:
                if (CustomerRouter.getDeeplinkClass() != null) {
                    mAppNotificationReceiverUIBackground.createNotification(data, CustomerRouter.getDeeplinkClass());
                } else {
                    return;
                }
                break;
            case TkpdState.GCMServiceState.GCM_CART:
                if (SessionHandler.isV4Login(this))
                    mAppNotificationReceiverUIBackground.createNotification(data, Cart.class);
                break;
            case TkpdState.GCMServiceState.GCM_WISHLIST:
                if (SessionHandler.isV4Login(this)) {
                    mAppNotificationReceiverUIBackground.createNotification(data, SimpleHomeRouter.getSimpleHomeActivityClass());
                }
                break;
            case TkpdState.GCMServiceState.GCM_VERIFICATION:
                if (SessionHandler.isV4Login(this)) {
                    mAppNotificationReceiverUIBackground.createNotification(data, ManageGeneral.class);
                } else {
                    data.putInt("keylogin1", TkpdState.DrawerPosition.LOGIN);
                    data.putInt("keylogin2", SessionView.HOME);
                    Intent intent = SessionRouter.getLoginActivityIntent(getApplicationContext());
                    mAppNotificationReceiverUIBackground.createNotification(data, intent.getClass());
                }
                break;
        }
    }

    private void handleDedicatedNotification(Bundle data) {
        if (SessionHandler.isV4Login(this)
                && SessionHandler.getLoginID(this).equals(data.getString("to_user_id"))) {
            resetNotificationStatus(data);

            if (mActivitiesLifecycleCallbacks.isAppOnBackground()) {
                mAppNotificationReceiverUIBackground.sendNotification(data);
            } else {
                NotificationReceivedListener listener =
                        (NotificationReceivedListener) mActivitiesLifecycleCallbacks.getLiveActivityOrNull();
                if (listener != null) {
                    listener.onGetNotif();
                    if (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))
                            == TkpdState.GCMServiceState.GCM_CART_UPDATE) {
                        listener.onRefreshCart(data.getInt("is_cart_exists", 0));
                    } else {
                        mAppNotificationReceiverUIBackground.sendNotification(data);
                    }
                } else {
                    if (isDedicatedNotification(data)) {
                        mAppNotificationReceiverUIBackground.sendNotification(data);
                    }
                }
            }
            cacheManager.resetCache(data);
        }
    }

    /**
     * Maintained Notification below 500
     */
    private boolean isDedicatedNotification(Bundle data) {
        return Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)) < 500;
    }

    private void sendMarketingPromoCode(Bundle data) {
        mAppNotificationReceiverUIBackground.sendNotification(data);
    }

    private void sendGeneralNotification(Bundle data) {
        mAppNotificationReceiverUIBackground.sendNotification(data);
    }


    private void resetNotificationStatus(Bundle data) {
        switch (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))) {
            case TkpdState.GCMServiceState.GCM_DRAWER_UPDATE:
                MainApplication.resetDrawerStatus(true);
                break;
            case TkpdState.GCMServiceState.GCM_CART_UPDATE:
                MainApplication.resetCartStatus(true);
                break;
            default:
                MainApplication.resetNotificationStatus(true);
                break;
        }
    }

    public interface OnGetFileListener {
        void onFileReady(File file);
    }
}
