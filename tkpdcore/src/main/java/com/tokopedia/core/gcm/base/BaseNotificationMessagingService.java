package com.tokopedia.core.gcm.base;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tokopedia.core.gcm.ActivitiesLifecycleCallbacks;
import com.tokopedia.core.gcm.AppNotificationReceiverUIBackground;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.GCMUtils;
import com.tokopedia.core.gcm.INotificationAnalyticsReceiver;
import com.tokopedia.core.gcm.NotificationAnalyticsReceiver;

/**
 * @author by alvarisi on 1/10/17.
 */

public abstract class BaseNotificationMessagingService extends FirebaseMessagingService {
    protected AppNotificationReceiverUIBackground mAppNotificationReceiverUIBackground;
    protected FCMCacheManager cacheManager;
    protected INotificationAnalyticsReceiver mNotificationAnalyticsReceiver;
    protected ActivitiesLifecycleCallbacks mActivitiesLifecycleCallbacks;
    protected Context mContext;

    public BaseNotificationMessagingService() {
        mAppNotificationReceiverUIBackground = new AppNotificationReceiverUIBackground(getApplication());
        mNotificationAnalyticsReceiver = new NotificationAnalyticsReceiver();
        mActivitiesLifecycleCallbacks = new ActivitiesLifecycleCallbacks(getApplication());
        mContext = getApplication().getApplicationContext();
    }

    protected Bundle convertMap(RemoteMessage message){
        return GCMUtils.convertMap(message.getData());
    }
}