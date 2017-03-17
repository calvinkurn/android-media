package com.tokopedia.tkpd.fcm;

import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.core.deeplink.CoreDeeplinkModuleLoader;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.gcm.Visitable;
import com.tokopedia.core.gcm.base.BaseAppNotificationReceiverUIBackground;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToBuyerEditNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToBuyerNotification;
import com.tokopedia.core.gcm.notification.promotions.DeeplinkNotification;
import com.tokopedia.core.gcm.utils.GCMUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.inbox.deeplink.InboxDeeplinkModuleLoader;
import com.tokopedia.tkpd.deeplink.ConsumerDeeplinkModuleLoader;
import com.tokopedia.tkpd.deeplink.DeepLinkDelegate;
import com.tokopedia.tkpd.fcm.notification.NewReviewNotification;
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
import com.tokopedia.tkpd.fcm.notification.ResCenterSellerAgreeNotification;
import com.tokopedia.tkpd.fcm.notification.ResCenterSellerReplyNotification;
import com.tokopedia.tkpd.fcm.notification.ReviewEditedNotification;
import com.tokopedia.tkpd.fcm.notification.ReviewReplyNotification;

import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * Created by alvarisi on 1/17/17.
 */

public class AppNotificationReceiverUIBackground extends BaseAppNotificationReceiverUIBackground {

    public AppNotificationReceiverUIBackground(Application application) {
        super(application);
    }

    @Override
    public void notifyReceiverBackgroundMessage(Observable<Bundle> data) {
        data.map(new Func1<Bundle, Boolean>() {
            @Override
            public Boolean call(Bundle bundle) {
                if (isAllowedNotification(bundle)) {
                    mFCMCacheManager.setCache();
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
                return true;
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Actions.empty(), new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private boolean isAllowedNotification(Bundle data) {
        return mFCMCacheManager.isAllowToHandleNotif(data)
                && mFCMCacheManager.checkLocalNotificationAppSettings(
                Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE)
                )
        );
    }

    private void handleApplinkNotification(Bundle data) {
        if (data.getString(Constants.ARG_NOTIFICATION_APPLINK_LOGIN_REQUIRED, "false").equals("true")) {
            if (SessionHandler.isV4Login(mContext)
                    && SessionHandler.getLoginID(mContext).equals(
                    data.getString(Constants.ARG_NOTIFICATION_TARGET_USER_ID))
                    ) {

                resetNotificationStatus(data);

                if (mActivitiesLifecycleCallbacks.isAppOnBackground()) {
                    prepareAndExecuteApplinkNotification(data);
                } else {
                    NotificationReceivedListener listener =
                            (NotificationReceivedListener) mActivitiesLifecycleCallbacks.getLiveActivityOrNull();
                    if (listener != null) {
                        listener.onGetNotif();
                        if (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))
                                == TkpdState.GCMServiceState.GCM_CART_UPDATE) {
                            listener.onRefreshCart(data.getInt(Constants.ARG_NOTIFICATION_CART_EXISTS, 0));
                        } else {
                            prepareAndExecuteApplinkNotification(data);
                        }
                    } else {
                        prepareAndExecuteApplinkNotification(data);
                    }
                }
                mFCMCacheManager.resetCache(data);
            }
        } else {
            prepareAndExecuteApplinkNotification(data);
        }
    }

    private void prepareAndExecuteApplinkNotification(Bundle data) {
        String applinks = data.getString(Constants.ARG_NOTIFICATION_APPLINK);
        String category = Uri.parse(applinks).getHost();
        String customIndex = "";
        String serverId = "";
        switch (category) {
            case Constants.ARG_NOTIFICATION_APPLINK_MESSAGE:
                customIndex = data.getString(Constants.ARG_NOTIFICATION_APPLINK_MESSAGE_CUSTOM_INDEX);
                if (!TextUtils.isEmpty(Uri.parse(applinks).getLastPathSegment())) {
                    serverId = Uri.parse(applinks).getLastPathSegment();
                }
                break;
            case Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION:
                customIndex = data.getString(Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION_CUSTOM_INDEX);
                if (!TextUtils.isEmpty(Uri.parse(applinks).getLastPathSegment())) {
                    serverId = Uri.parse(applinks).getLastPathSegment();
                }
                break;
        }

        saveApplinkPushNotification(
                category,
                convertBundleToJsonString(data),
                customIndex,
                serverId,
                new SavePushNotificationCallback()
        );
    }


    public void handleDedicatedNotification(Bundle data) {
        if (SessionHandler.isV4Login(mContext)
                && SessionHandler.getLoginID(mContext).equals(data.getString(Constants.ARG_NOTIFICATION_TARGET_USER_ID))) {

            resetNotificationStatus(data);

            if (mActivitiesLifecycleCallbacks.isAppOnBackground()) {
                prepareAndExecuteDedicatedNotification(data);
            } else {
                NotificationReceivedListener listener =
                        (NotificationReceivedListener) mActivitiesLifecycleCallbacks.getLiveActivityOrNull();
                if (listener != null) {
                    listener.onGetNotif();
                    if (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))
                            == TkpdState.GCMServiceState.GCM_CART_UPDATE) {
                        listener.onRefreshCart(data.getInt(Constants.ARG_NOTIFICATION_CART_EXISTS, 0));
                    } else {
                        prepareAndExecuteDedicatedNotification(data);
                    }
                } else {
                    prepareAndExecuteDedicatedNotification(data);
                }
            }
            mFCMCacheManager.resetCache(data);
        }
    }

    @Override
    public void handlePromotionNotification(Bundle data) {

    }

    private boolean isSupportedApplinkNotification(Bundle bundle) {
        String applink = bundle.getString(Constants.ARG_NOTIFICATION_APPLINK, "");
        DeepLinkDelegate deepLinkDelegate = new DeepLinkDelegate(
                new ConsumerDeeplinkModuleLoader(),
                new CoreDeeplinkModuleLoader(),
                new InboxDeeplinkModuleLoader()
        );
        return deepLinkDelegate.supportsUri(applink);
    }

    private void prepareAndExecutePromoNotification(Bundle data) {
        Map<Integer, Class> promoNotifications = getCommonPromoNotification();
        promoNotifications.put(TkpdState.GCMServiceState.GCM_DEEPLINK, DeeplinkNotification.class);
        Class<?> clazz = promoNotifications.get(GCMUtils.getCode(data));
        if (clazz != null) {
            executeNotification(data, clazz);
        }
    }

    private void prepareAndExecuteDedicatedNotification(Bundle data) {
        Map<Integer, Visitable> visitableMap = getCommonDedicatiedObject();
        Visitable visitable = visitableMap.get(GCMUtils.getCode(data));
        if (visitable != null){
            visitable.proccessReceivedNotification(data);
        }
        /*Map<Integer, Class> dedicatedNotification = getCommonDedicatedNotification();
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REVIEW, NewReviewNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REVIEW_EDIT, ReviewEditedNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REVIEW_REPLY, ReviewReplyNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_BUYER, ReputationSmileyToBuyerNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_BUYER, ReputationSmileyToBuyerEditNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_VERIFIED, PurchaseVerifiedNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_ACCEPTED, PurchaseAcceptedNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_PARTIAL_PROCESSED, PurchasePartialProcessedNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_REJECTED, PurchaseRejectedNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_DELIVERED, PurchaseDeliveredNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_REPLY, ResCenterSellerReplyNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_AGREE, ResCenterSellerAgreeNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_BUYER_REPLY, ResCenterAdminBuyerReplyNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_AUTO_CANCEL_2D, PurchaseAutoCancel2DNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_AUTO_CANCEL_4D, PurchaseAutoCancel4DNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_FINISH, PurchaseFinishedNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_FINISH_REMINDER, PurchaseFinishReminderNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_NEW_ORDER, PurchaseNewOrderNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_REJECTED_SHIPPING, PurchaseRejectedShippingNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_CONFIRM_SHIPPING, PurchaseShippedNotification.class);

        Class<?> clazz = dedicatedNotification.get(GCMUtils.getCode(data));
        if (clazz != null) {
            executeNotification(data, clazz);
        }*/
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
