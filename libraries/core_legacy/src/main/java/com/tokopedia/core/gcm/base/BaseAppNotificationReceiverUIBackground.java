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
    protected Context mContext;

    public BaseAppNotificationReceiverUIBackground(Application application) {
        mContext = application.getApplicationContext();
    }

    public abstract void notifyReceiverBackgroundMessage(Bundle bundle);
}