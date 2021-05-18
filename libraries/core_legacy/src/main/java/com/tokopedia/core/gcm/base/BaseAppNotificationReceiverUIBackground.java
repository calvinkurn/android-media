package com.tokopedia.core.gcm.base;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.tkpd.library.utils.legacy.CommonUtils;
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
import com.tokopedia.usecase.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import rx.Subscriber;

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
        PushNotificationRepository pushNotificationRepository = new PushNotificationDataRepository(mContext);
        mSavePushNotificationUseCase = new SavePushNotificationUseCase(
                pushNotificationRepository
        );
    }

    public abstract void notifyReceiverBackgroundMessage(Bundle bundle);
}