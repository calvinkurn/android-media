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
import com.tokopedia.core.gcm.notification.dedicated.NewReviewNotification;
import com.tokopedia.core.gcm.notification.dedicated.PurchaseDisputeNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToSellerEditNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReputationSmileyToSellerNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterAdminSellerReplyNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterNewNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterSellerAgreeNotification;
import com.tokopedia.core.gcm.notification.dedicated.ResCenterSellerReplyNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReviewEditedNotification;
import com.tokopedia.core.gcm.notification.dedicated.ReviewReplyNotification;
import com.tokopedia.core.gcm.notification.dedicated.SellingAutoCancel2DNotification;
import com.tokopedia.core.gcm.notification.dedicated.SellingAutoCancel4DNotification;
import com.tokopedia.core.gcm.notification.dedicated.SellingInvalidResiNotification;
import com.tokopedia.core.gcm.notification.dedicated.SellingOrderDeliveredNotification;
import com.tokopedia.core.gcm.notification.dedicated.SellingOrderFinishedNotification;
import com.tokopedia.core.gcm.notification.dedicated.TicketResponseNotification;
import com.tokopedia.core.gcm.utils.ActivitiesLifecycleCallbacks;
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

    protected static int getCode(Bundle data) {
        int code;
        try {
            code = Integer.parseInt(data.getString("tkp_code", "0"));
        } catch (NumberFormatException e) {
            code = 0;
        }
        return code;
    }

    protected boolean isDedicatedNotification(Bundle data) {
        //disable message push notif
        return getCode(data) != 101 && (getCode(data) < 1000 || getCode(data) >= 1100);
    }

    protected void resetNotificationStatus(Bundle data) {
        switch (Integer.parseInt(data.getString(ARG_NOTIFICATION_CODE, "0"))) {
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

    public abstract void notifyReceiverBackgroundMessage(Bundle bundle);

    protected Map<Integer, Visitable> getCommonDedicatiedObject() {
        Map<Integer, Visitable> dedicatedNotification = new HashMap<>();
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_MESSAGE, new NewMessageNotification(mContext));
        return dedicatedNotification;
    }

    protected Map<Integer, Visitable> getCommonDedicatedNotification() {
        Map<Integer, Visitable> dedicatedNotification = new HashMap<>();
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_MESSAGE, new NewMessageNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_TALK, new NewDiscussionNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REVIEW, new NewReviewNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REVIEW_EDIT, new ReviewEditedNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REVIEW_REPLY, new ReviewReplyNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_TICKET, new TicketResponseNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RES_CENTER, new ResCenterNewNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_NEWORDER, new NewOrderNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_SMILEY_TO_SELLER, new ReputationSmileyToSellerNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_REPUTATION_EDIT_SMILEY_TO_SELLER, new ReputationSmileyToSellerEditNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_PURCHASE_DISPUTE, new PurchaseDisputeNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_ADMIN_SELLER_REPLY, new ResCenterAdminSellerReplyNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_ORDER_CANCEL_2D_SELLER, new SellingAutoCancel2DNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_ORDER_CANCEL_4D_SELLER, new SellingAutoCancel4DNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_ORDER_INVALID_RESI, new SellingInvalidResiNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_ORDER_DELIVERED_SELLER, new SellingOrderDeliveredNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_ORDER_FINISH_SELLER, new SellingOrderFinishedNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_REPLY, new ResCenterSellerReplyNotification(mContext));
        dedicatedNotification.put(TkpdState.GCMServiceState.GCM_RESCENTER_SELLER_AGREE, new ResCenterSellerAgreeNotification(mContext));
        return dedicatedNotification;
    }

    protected Map<Integer, Visitable> getCommonPromoNotification() {
        Map<Integer, Visitable> promotionsNotification = new HashMap<>();
        return promotionsNotification;
    }

    protected void executeNotification(Bundle data, Class<?> clazz) {
        Constructor<?> ctor = null;
        CommonUtils.dumper("executeNotification");
        try {
            ctor = clazz.asSubclass(clazz).getConstructor(Context.class);
        } catch (NoSuchMethodException e) {
            CommonUtils.dumper(clazz.toString());
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

    protected String convertBundleToJsonString(Bundle bundle) {
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                json.put(key, bundle.getString(key));
            } catch (JSONException e) {
                return null;
            }
        }
        return json.toString();
    }


    public interface OnSavePushNotificationCallback {
        void onSuccessSavePushNotification(String category);

        void onFailedSavePushNotification();
    }

}