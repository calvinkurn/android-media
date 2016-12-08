package com.tokopedia.core.notification.services;

import android.app.IntentService;
import android.content.Intent;

import com.tokopedia.core.notification.interactor.NotificationDataInteractor;
import com.tokopedia.core.notification.model.FcmClientIdUpdate;

/**
 * @author  by alvarisi on 12/8/16.
 */

public class NotificationIntentService extends IntentService {
    public static final String ARG_EXTRA_GCM_UPDATE = "ARG_EXTRA_GCM_UPDATE";
    public static final int CODE_EXTRA_GCM_UPDATE = 1001;
    private NotificationDataInteractor mInteractor;
    public NotificationIntentService() {
        super(NotificationIntentService.class.getCanonicalName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int code = intent.getIntExtra(ARG_EXTRA_GCM_UPDATE, 0);
        switch (code){
            case CODE_EXTRA_GCM_UPDATE:

                break;
        }

    }
}