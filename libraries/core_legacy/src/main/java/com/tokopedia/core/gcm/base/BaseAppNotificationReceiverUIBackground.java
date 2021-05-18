package com.tokopedia.core.gcm.base;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.data.PushNotificationDataRepository;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.domain.usecase.SavePushNotificationUseCase;
import com.tokopedia.core.gcm.utils.ActivitiesLifecycleCallbacks;

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