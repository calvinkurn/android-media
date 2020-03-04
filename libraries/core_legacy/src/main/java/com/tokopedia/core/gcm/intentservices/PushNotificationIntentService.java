package com.tokopedia.core.gcm.intentservices;

import android.app.IntentService;
import android.content.Intent;

import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.data.PushNotificationDataInteractor;
import com.tokopedia.core.gcm.data.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;

import rx.Subscriber;
import timber.log.Timber;

/**
 * @author by alvarisi on 12/8/16.
 */

public class PushNotificationIntentService extends IntentService {
    public static final String ARG_EXTRA_GCM_UPDATE = "ARG_EXTRA_GCM_UPDATE";
    public static final String ARG_EXTRA_GCM_UPDATE_DATA = "ARG_EXTRA_GCM_UPDATE_DATA";
    public static final String UPDATE_NOTIFICATION_DATA = "update_notification_data";
    public static final String BROADCAST_GET_NOTIFICATION = "broadcast_get_notification";
    public static final String GET_NOTIFICATION_SUCCESS = "get_notification_success";
    public static final int CODE_EXTRA_GCM_UPDATE = 1001;
    private PushNotificationDataInteractor mInteractor;

    public PushNotificationIntentService() {
        super(PushNotificationIntentService.class.getCanonicalName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null){
            return;
        }
        int code = intent.getIntExtra(ARG_EXTRA_GCM_UPDATE, 0);
        switch (code) {
            case CODE_EXTRA_GCM_UPDATE:
                FCMTokenUpdate data = intent.getParcelableExtra(ARG_EXTRA_GCM_UPDATE_DATA);
                handleUpdateClientId(data);
                break;
        }
    }

    private void handleUpdateClientId(FCMTokenUpdate data) {
        if (mInteractor == null) mInteractor = new PushNotificationDataInteractor(getApplicationContext());
        mInteractor.updateTokenServer(data, new UpdateClientIdSubscriber());
    }

    private class UpdateClientIdSubscriber extends Subscriber<FCMTokenUpdateEntity> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(FCMTokenUpdateEntity entity) {
            Timber.d(entity.toString());
            if (entity.getSuccess()) {
                FCMCacheManager.storeFcmTimestamp(getApplicationContext());
                FCMCacheManager.storeRegId(entity.getToken(), getBaseContext());
            }
        }
    }
}