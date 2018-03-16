package com.tokopedia.tkpd.fcm;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.gcm.Visitable;
import com.tokopedia.core.gcm.base.BaseAppNotificationReceiverUIBackground;
import com.tokopedia.core.gcm.notification.applink.ApplinkPushNotificationBuildAndShow;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToBuyerEditNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToBuyerNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterBuyerAgreeNotification;
import com.tokopedia.core.gcm.notification.promotions.CartNotification;
import com.tokopedia.core.gcm.notification.promotions.DeeplinkNotification;
import com.tokopedia.core.gcm.notification.promotions.GeneralNotification;
import com.tokopedia.core.gcm.notification.promotions.PromoNotification;
import com.tokopedia.core.gcm.notification.promotions.WishlistNotification;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.inbox.inboxchat.ChatNotifInterface;
import com.tokopedia.pushnotif.ApplinkNotificationHelper;
import com.tokopedia.ride.deeplink.RidePushNotificationBuildAndShow;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.fcm.applink.ApplinkBuildAndShowNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseAcceptedNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseAutoCancel2DNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseAutoCancel4DNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseDeliveredNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseFinishReminderNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseFinishedNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseNewOrderNotification;
import com.tokopedia.tkpd.fcm.notification.PurchasePartialProcessedNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseRejectedNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseRejectedShippingNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseShippedNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseVerifiedNotification;
import com.tokopedia.tkpd.fcm.notification.ResCenterAdminBuyerReplyNotification;
import com.tokopedia.tkpd.fcm.notification.ResCenterBuyerReplyNotification;

import java.util.Map;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;


/**
 * Created by alvarisi on 1/17/17.
 */

public class AppNotificationReceiverUIBackground extends BaseAppNotificationReceiverUIBackground {
    private static final String DEFAULT_NOTIF_CODE_VALUE = "0";
    private static final int DEFAULT_CART_VALUE = 0;
    private static final int DEFAULT_RIDE_URL_SIZE = 1;
    private RemoteConfig remoteConfig;

    public AppNotificationReceiverUIBackground(Application application) {
        super(application);
        remoteConfig = new FirebaseRemoteConfigImpl(application);
    }

    @Override
    public void notifyReceiverBackgroundMessage(Bundle bundle) {
        if (isAllowedNotification(bundle)) {
            mFCMCacheManager.setCache();
            if (isApplinkNotification(bundle)) {
                ApplinkNotificationHelper applinkNotificationHelper = new ApplinkNotificationHelper(mContext);
                applinkNotificationHelper.notifyApplinkNotification(bundle);
            } else {
                //TODO this function for divide the new and old flow(that still supported)
                // next if complete new plz to delete
                if (isSupportedApplinkNotification(bundle)) {
                    handleApplinkNotification(bundle);
                } else {
                    if (isDedicatedNotification(bundle)) {
                        handleDedicatedNotification(bundle);
                    } else {
                        prepareAndExecutePromoNotification(bundle);
                    }
                }
            }
        }
    }

    private boolean isApplinkNotification(Bundle data) {
        return !data.getString(Constants.ARG_NOTIFICATION_APPLINK, "").equals("");
    }

    private boolean isAllowedNotification(Bundle data) {
        return mFCMCacheManager.isAllowToHandleNotif(data)
                && mFCMCacheManager.checkLocalNotificationAppSettings(
                Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE, "0")
                )
        );
    }

    private void handleApplinkNotification(Bundle data) {
        if (data.getString(Constants.ARG_NOTIFICATION_APPLINK_LOGIN_REQUIRED, "false").equals("true")) {
            if (SessionHandler.getLoginID(mContext).equals(
                    data.getString(Constants.ARG_NOTIFICATION_TARGET_USER_ID))
            ) {
                resetNotificationStatus(data);
                prepareAndExecuteApplinkNotification(data);
                refreshUI(data);
                mFCMCacheManager.resetCache(data);
            }
        } else {
            if (data.getString(Constants.KEY_ORIGIN, "").equals(Constants.ARG_NOTIFICATION_APPLINK_PROMO_LABEL)) {
                prepareAndExecuteApplinkPromoNotification(data);
            } else {
                prepareAndExecuteApplinkNotification(data);
            }
        }
    }

    private void prepareAndExecuteApplinkNotification(Bundle data) {
        if (!isRefreshCart(data)) {
            String applinks = data.getString(Constants.ARG_NOTIFICATION_APPLINK);
            String category = Uri.parse(applinks).getHost();
            String customIndex = "";
            String serverId = "";
            switch (category) {
                case Constants.ARG_NOTIFICATION_APPLINK_MESSAGE:
                    if (!remoteConfig.getBoolean(TkpdInboxRouter.ENABLE_TOPCHAT)) {
                        customIndex = data.getString(Constants.ARG_NOTIFICATION_APPLINK_MESSAGE_CUSTOM_INDEX);
                        if (!TextUtils.isEmpty(Uri.parse(applinks).getLastPathSegment())) {
                            serverId = Uri.parse(applinks).getLastPathSegment();
                        }
                        saveApplinkPushNotification(
                                category,
                                convertBundleToJsonString(data),
                                customIndex,
                                serverId,
                                new SavePushNotificationCallback()
                        );
                    }
                    break;
                case Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION:
                    customIndex = data.getString(Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION_CUSTOM_INDEX);
                    if (!TextUtils.isEmpty(Uri.parse(applinks).getLastPathSegment())) {
                        serverId = Uri.parse(applinks).getLastPathSegment();
                    }
                    saveApplinkPushNotification(
                            category,
                            convertBundleToJsonString(data),
                            customIndex,
                            serverId,
                            new SavePushNotificationCallback()
                    );
                    break;
                case Constants.ARG_NOTIFICATION_APPLINK_RIDE:
                    if (Uri.parse(applinks).getPathSegments().size() == DEFAULT_RIDE_URL_SIZE) {
                        buildNotifByData(data);
                    } else {
                        CommonUtils.dumper("AppNotificationReceiverUIBackground handleApplinkNotification for Ride");
                        RidePushNotificationBuildAndShow push = new RidePushNotificationBuildAndShow(mContext);
                        push.processReceivedNotification(data);
                    }
                    break;

                case Constants.ARG_NOTIFICATION_APPLINK_TOPCHAT:
                    if (remoteConfig.getBoolean(TkpdInboxRouter.ENABLE_TOPCHAT)) {
                        if (mActivitiesLifecycleCallbacks.getLiveActivityOrNull() != null
                                && mActivitiesLifecycleCallbacks.getLiveActivityOrNull() instanceof ChatNotifInterface) {
                            ((ChatNotifInterface) mActivitiesLifecycleCallbacks.getLiveActivityOrNull()).onGetNotif(data);
                        } else {
                            String applink = data.getString(Constants.ARG_NOTIFICATION_APPLINK);
                            String fullname = data
                                    .getString("full_name");
                            applink += "?" + "fullname=" + fullname;
                            data.putString(Constants.ARG_NOTIFICATION_APPLINK, applink);
                            buildNotifByData(data);
                        }
                    }
                    break;
                case Constants.ARG_NOTIFICATION_APPLINK_SELLER_INFO:
                    if (SessionHandler.isUserHasShop(mContext)) {
                        buildNotifByData(data);
                    }
                    break;
                default:
                    buildNotifByData(data);
                    break;
            }
        }
    }

    private void buildNotifByData(Bundle data) {
        ApplinkPushNotificationBuildAndShow buildAndShow = new ApplinkPushNotificationBuildAndShow(data);
        Intent intent = new Intent(mContext, DeeplinkHandlerActivity.class);
        buildAndShow.process(mContext, intent);
    }


    public void handleDedicatedNotification(Bundle data) {
        if (SessionHandler.isV4Login(mContext)
                && SessionHandler.getLoginID(mContext).equals(data.getString(Constants.ARG_NOTIFICATION_TARGET_USER_ID))) {

            resetNotificationStatus(data);
            prepareAndExecuteDedicatedNotification(data);
            refreshUI(data);
            mFCMCacheManager.resetCache(data);
        }
    }

    private void refreshUI(Bundle data) {
        if (!mActivitiesLifecycleCallbacks.isAppOnBackground()) {
            Activity currentActivity = mActivitiesLifecycleCallbacks.getLiveActivityOrNull();
            if(currentActivity != null && currentActivity instanceof NotificationReceivedListener) {
                NotificationReceivedListener listener = (NotificationReceivedListener) currentActivity;
                listener.onGetNotif();
                if (isRefreshCart(data)) {
                    listener.onRefreshCart(data.getInt(Constants.ARG_NOTIFICATION_CART_EXISTS, DEFAULT_CART_VALUE));
                }
            }
        }
    }

    @Override
    public void handlePromotionNotification(Bundle data) {

    }

    private boolean isSupportedApplinkNotification(Bundle bundle) {
        String applink = bundle.getString(Constants.ARG_NOTIFICATION_APPLINK, "");
        return ((TkpdCoreRouter) mContext.getApplicationContext())
                .isSupportedDelegateDeepLink(applink);

    }

    private void prepareAndExecutePromoNotification(Bundle data) {
        Map<Integer, Visitable> promoNotifications = getCommonPromoNotification();
        promoNotifications.put(TkpdState.GCMServiceState.GCM_PROMO, new PromoNotification(mContext));
        promoNotifications.put(TkpdState.GCMServiceState.GCM_GENERAL, new GeneralNotification(mContext));
        promoNotifications.put(TkpdState.GCMServiceState.GCM_CART, new CartNotification(mContext));
        promoNotifications.put(TkpdState.GCMServiceState.GCM_WISHLIST, new WishlistNotification(mContext));
        promoNotifications.put(TkpdState.GCMServiceState.GCM_DEEPLINK, new DeeplinkNotification(mContext));
        Visitable visitable = promoNotifications.get(getCode(data));
        if (visitable != null) {
            visitable.proccessReceivedNotification(data);
        }
    }

    private void prepareAndExecuteApplinkPromoNotification(Bundle data) {
        ApplinkBuildAndShowNotification applinkBuildAndShowNotification =
                new ApplinkBuildAndShowNotification(AppNotificationReceiverUIBackground.this.mContext);
        applinkBuildAndShowNotification.showPromoNotification(data);
    }

    private void prepareAndExecuteDedicatedNotification(Bundle data) {
        if (!isRefreshCart(data)){
            Map<Integer, Visitable> visitables = getCommonDedicatedNotification();
            visitables.put(TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_BUYER, new ReputationSmileyToBuyerNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_BUYER, new ReputationSmileyToBuyerEditNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_PURCHASE_VERIFIED, new PurchaseVerifiedNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_PURCHASE_ACCEPTED, new PurchaseAcceptedNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_PURCHASE_PARTIAL_PROCESSED, new PurchasePartialProcessedNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_PURCHASE_REJECTED, new PurchaseRejectedNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_PURCHASE_DELIVERED, new PurchaseDeliveredNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_AGREE, new ResCenterBuyerAgreeNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_BUYER_REPLY, new ResCenterAdminBuyerReplyNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_PURCHASE_AUTO_CANCEL_2D, new PurchaseAutoCancel2DNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_PURCHASE_AUTO_CANCEL_4D, new PurchaseAutoCancel4DNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_PURCHASE_FINISH, new PurchaseFinishedNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_PURCHASE_FINISH_REMINDER, new PurchaseFinishReminderNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_PURCHASE_NEW_ORDER, new PurchaseNewOrderNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_PURCHASE_REJECTED_SHIPPING, new PurchaseRejectedShippingNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_PURCHASE_CONFIRM_SHIPPING, new PurchaseShippedNotification(mContext));
            visitables.put(TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_REPLY, new ResCenterBuyerReplyNotification(mContext));

            Visitable visitable = visitables.get(getCode(data));
            if (visitable != null) {
                visitable.proccessReceivedNotification(data);
            }
        }
    }

    private boolean isRefreshCart(Bundle data) {
        return Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE, DEFAULT_NOTIF_CODE_VALUE))
                == TkpdState.GCMServiceState.GCM_CART_UPDATE;
    }

    private class SavePushNotificationCallback implements OnSavePushNotificationCallback {
        @Override
        public void onSuccessSavePushNotification(String category) {
            ApplinkBuildAndShowNotification applinkBuildAndShowNotification =
                    new ApplinkBuildAndShowNotification(AppNotificationReceiverUIBackground.this.mContext);
            applinkBuildAndShowNotification.show(category);
        }

        @Override
        public void onFailedSavePushNotification() {

        }
    }
}
