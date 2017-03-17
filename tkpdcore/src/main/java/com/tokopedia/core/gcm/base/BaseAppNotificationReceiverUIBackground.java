package com.tokopedia.core.gcm.base;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.Visitable;
import com.tokopedia.core.gcm.data.PushNotificationDataRepository;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.domain.usecase.SavePushNotificationUseCase;
import com.tokopedia.core.gcm.notification.dedicated.NewDiscussionNotification;
import com.tokopedia.core.gcm.notification.dedicated.NewMessageNotification;
import com.tokopedia.core.gcm.notification.dedicated.NewOrderNotification;
import com.tokopedia.core.gcm.notification.dedicated.PurchaseDisputeNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToSellerEditNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToSellerNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterAdminSellerReplyNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterBuyerAgreeNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterBuyerReplyNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterNewNotification;
import com.tokopedia.core.gcm.notification.dedicated.SellingAutoCancel2DNotification;
import com.tokopedia.core.gcm.notification.dedicated.SellingAutoCancel4DNotification;
import com.tokopedia.core.gcm.notification.dedicated.SellingInvalidResiNotification;
import com.tokopedia.core.gcm.notification.dedicated.SellingOrderDeliveredNotification;
import com.tokopedia.core.gcm.notification.dedicated.SellingOrderFinishedNotification;
import com.tokopedia.core.gcm.notification.dedicated.TicketResponseNotification;
import com.tokopedia.core.gcm.notification.promotions.CartNotification;
import com.tokopedia.core.gcm.notification.promotions.GeneralNotification;
import com.tokopedia.core.gcm.notification.promotions.PromoNotification;
import com.tokopedia.core.gcm.notification.promotions.VerificationNotification;
import com.tokopedia.core.gcm.notification.promotions.WishlistNotification;
import com.tokopedia.core.gcm.utils.ActivitiesLifecycleCallbacks;
import com.tokopedia.core.gcm.utils.GCMUtils;
import com.tokopedia.core.var.TkpdState;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;

import static com.tokopedia.core.gcm.Constants.ARG_NOTIFICATION_CODE;

/**
 * Created by alvarisi on 1/18/17.
 */

public abstract class BaseAppNotificationReceiverUIBackground {
    protected FCMCacheManager mFCMCacheManager;
    protected Context mContext;
    protected ActivitiesLifecycleCallbacks mActivitiesLifecycleCallbacks;
    protected SavePushNotificationUseCase mSavePushNotificationUseCase;

    public interface OnSavePushNotificationCallback {
        void onSuccessSavePushNotification(String category);

        void onFailedSavePushNotification();
    }

    public BaseAppNotificationReceiverUIBackground(Application application) {
        mFCMCacheManager = new FCMCacheManager(application.getBaseContext());
        mContext = application.getApplicationContext();
        mActivitiesLifecycleCallbacks = new ActivitiesLifecycleCallbacks(application);
        PushNotificationRepository pushNotificationRepository = new PushNotificationDataRepository();
        mSavePushNotificationUseCase = new SavePushNotificationUseCase(
                new JobExecutor(),
                new UIThread(),
                pushNotificationRepository
        );
    }

    protected boolean isDedicatedNotification(Bundle data) {
        return GCMUtils.getCode(data) < 1000 || GCMUtils.getCode(data) >= 1100;
    }

    protected void resetNotificationStatus(Bundle data) {
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

    public abstract void handleDedicatedNotification(Bundle data);

    public abstract void handlePromotionNotification(Bundle data);

    public abstract void notifyReceiverBackgroundMessage(Observable<Bundle> data);

    protected Map<Integer, Class> getCommonDedicatedNotification() {
        Map<Integer, Class> dedicatedNotification = new HashMap<>();
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_MESSAGE, NewMessageNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_TALK, NewDiscussionNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_TICKET, TicketResponseNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RES_CENTER, ResCenterNewNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_NEWORDER, NewOrderNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_SELLER, ReputationSmileyToSellerNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_SELLER, ReputationSmileyToSellerEditNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_DISPUTE, PurchaseDisputeNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_REPLY, ResCenterBuyerReplyNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_BUYER_AGREE, ResCenterBuyerAgreeNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_SELLER_REPLY, ResCenterAdminSellerReplyNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_ORDER_CANCEL_2D_SELLER, SellingAutoCancel2DNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_ORDER_CANCEL_4D_SELLER, SellingAutoCancel4DNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_ORDER_INVALID_RESI, SellingInvalidResiNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_ORDER_DELIVERED_SELLER, SellingOrderDeliveredNotification.class);
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_ORDER_FINISH_SELLER, SellingOrderFinishedNotification.class);
        return dedicatedNotification;
    }

    protected Map<Integer, Class> getCommonPromoNotification() {
        Map<Integer, Class> promotionsNotification = new HashMap<>();
        promotionsNotification.put(TkpdState.GCMServiceState.GCM_PROMO, PromoNotification.class);
        promotionsNotification.put(TkpdState.GCMServiceState.GCM_GENERAL, GeneralNotification.class);
        promotionsNotification.put(TkpdState.GCMServiceState.GCM_CART, CartNotification.class);
        promotionsNotification.put(TkpdState.GCMServiceState.GCM_VERIFICATION, VerificationNotification.class);
        promotionsNotification.put(TkpdState.GCMServiceState.GCM_WISHLIST, WishlistNotification.class);
        return promotionsNotification;
    }

    protected void executeNotification(Bundle data, Class<?> clazz) {
        Constructor<?> ctor = null;
        CommonUtils.dumper("executeNotification");
        try {
            ctor = clazz.getConstructor(Context.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
        Object object = null;
        try {
            object = ctor.newInstance(mContext);
        } catch (InstantiationException e) {
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return;
        }

        if (object != null && object instanceof Visitable) {
            CommonUtils.dumper("object instanceof Visitable");
            ((Visitable) object).proccessReceivedNotification(data);
        }
    }

    protected void saveApplinkPushNotification(String category,
                                               String response,
                                               String customIndex,
                                               String serverId,
                                               final OnSavePushNotificationCallback callback) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SavePushNotificationUseCase.KEY_CATEGORY, category);
        requestParams.putString(SavePushNotificationUseCase.KEY_RESPONSE, response);
        requestParams.putString(SavePushNotificationUseCase.KEY_CUSTOM_INDEX, customIndex);
        requestParams.putString(SavePushNotificationUseCase.KEY_SERVER_ID, serverId);
        mSavePushNotificationUseCase.execute(requestParams, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callback.onFailedSavePushNotification();
            }

            @Override
            public void onNext(String string) {
                if (!TextUtils.isEmpty(string)) {
                    callback.onSuccessSavePushNotification(string);
                } else {
                    callback.onFailedSavePushNotification();
                }
            }
        });
    }

    protected String convertBundleToJsonString(Bundle bundle){
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                json.put(key, bundle.getString(key));
            } catch(JSONException e) {
                return null;
            }
        }
        return json.toString();
    }

}