package com.tokopedia.pushnotif;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.PushNotificationGeneratedDatabaseHolder;

/**
 * @author ricoharisin .
 */

public class PushNotification {

    public static void initDatabase(Context applicationContext) {
        try{
            FlowManager.getConfig();
        } catch (IllegalStateException e) {
            FlowManager.init(new FlowConfig.Builder(applicationContext).build());
        }
        FlowManager.initModule(PushNotificationGeneratedDatabaseHolder.class);
    }
}
