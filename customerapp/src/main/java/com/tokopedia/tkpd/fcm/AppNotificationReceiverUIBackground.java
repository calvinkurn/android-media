package com.tokopedia.tkpd.fcm;

import android.app.Application;
import android.os.Bundle;

import com.tokopedia.core.gcm.NotificationReceivedListener;
import com.tokopedia.core.gcm.base.BaseAppNotificationReceiverUIBackground;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToBuyerEditNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToBuyerNotification;
import com.tokopedia.core.gcm.utils.GCMUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.tkpd.fcm.notification.NewReviewNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseAcceptedNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseDeliveredNotification;
import com.tokopedia.tkpd.fcm.notification.PurchasePartialProcessedNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseRejectedNotification;
import com.tokopedia.tkpd.fcm.notification.PurchaseVerifiedNotification;
import com.tokopedia.tkpd.fcm.notification.ResCenterAdminBuyerReplyNotification;
import com.tokopedia.tkpd.fcm.notification.ResCenterSellerAgreeNotification;
import com.tokopedia.tkpd.fcm.notification.ResCenterSellerReplyNotification;
import com.tokopedia.tkpd.fcm.notification.ReviewEditedNotification;
import com.tokopedia.tkpd.fcm.notification.ReviewReplyNotification;

import java.util.Map;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * Created by alvarisi on 1/17/17.
 */

public class AppNotificationReceiverUIBackground extends BaseAppNotificationReceiverUIBackground {

    public AppNotificationReceiverUIBackground(Application application) {
        super(application);
    }

    public void notifyReceiverBackgroundMessage(Bundle data) {
        if (isDedicatedNotification(data)) {
            handleDedicatedNotification(data);
        } else {
            handlePromotionNotification(data);
        }
    }

    public void handleDedicatedNotification(Bundle data) {
        if (SessionHandler.isV4Login(mContext)
                && SessionHandler.getLoginID(mContext).equals(data.getString("to_user_id"))) {

            resetNotificationStatus(data);

            if (mActivitiesLifecycleCallbacks.isAppOnBackground()) {
                prepareDeathOrLiveFunction(data);
            } else {
                NotificationReceivedListener listener =
                        (NotificationReceivedListener) mActivitiesLifecycleCallbacks.getLiveActivityOrNull();
                if (listener != null) {
                    listener.onGetNotif();
                    if (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE))
                            == TkpdState.GCMServiceState.GCM_CART_UPDATE) {
                        listener.onRefreshCart(data.getInt("is_cart_exists", 0));
                    } else {
                        prepareDeathOrLiveFunction(data);
                    }
                } else {
                    prepareDeathOrLiveFunction(data);
                }
            }
            mFCMCacheManager.resetCache(data);
        }
    }



    private void handlePromotionNotification(Bundle data) {
        prepareAndExecutePromoNotification(data);
    }

    private void prepareAndExecutePromoNotification(Bundle data) {
        Map<Integer, Class> dedicatedNotification = getCommonPromoNotification();
        Class<?> clazz = dedicatedNotification.get(GCMUtils.getCode(data));
        if (clazz != null) {
            suchADangerousReflectionFunction(data, clazz);
        }
    }

    private void prepareDeathOrLiveFunction(Bundle data) {
        Map<Integer, Class> dedicatedNotification = getCommonDedicatedNotification();
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

        Class<?> clazz = dedicatedNotification.get(GCMUtils.getCode(data));
        if (clazz != null) {
            suchADangerousReflectionFunction(data, clazz);
        }
    }
}
