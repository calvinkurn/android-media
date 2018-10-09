package com.tokopedia.core.gcm.notification.applink;

import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.NotificationConfiguration;

/**
 * Created by alvarisi on 2/23/17.
 */

public abstract class AbstractApplinkBuildAndShowNotification<T> {
    public abstract void process(Context context, Intent handlerIntent);

    public abstract void process(Context context, Intent handlerIntent, boolean isNew);

    protected NotificationConfiguration buildDefaultConfiguration(Context context){
        FCMCacheManager fcmCacheManager = new FCMCacheManager(context);
        NotificationConfiguration configuration = new NotificationConfiguration();
        configuration.setBell(fcmCacheManager.isAllowBell());
        configuration.setVibrate(fcmCacheManager.isVibrate());
        configuration.setSoundUri(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        return configuration;
    }
}