package com.tokopedia.core.notification.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author  by alvarisi on 12/8/16.
 */

public class NotificationIntentService extends IntentService {
    public NotificationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
