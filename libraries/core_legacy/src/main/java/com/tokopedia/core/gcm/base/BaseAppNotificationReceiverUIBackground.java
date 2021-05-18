package com.tokopedia.core.gcm.base;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.utils.ActivitiesLifecycleCallbacks;

/**
 * Created by alvarisi on 1/18/17.
 */

public abstract class BaseAppNotificationReceiverUIBackground {
    protected FCMCacheManager mFCMCacheManager;
    protected Context mContext;
    protected ActivitiesLifecycleCallbacks mActivitiesLifecycleCallbacks;

    public BaseAppNotificationReceiverUIBackground(Application application) {
        mFCMCacheManager = new FCMCacheManager(application.getBaseContext());
        mContext = application.getApplicationContext();
        mActivitiesLifecycleCallbacks = new ActivitiesLifecycleCallbacks(application);

    }

    public abstract void notifyReceiverBackgroundMessage(Bundle bundle);
}