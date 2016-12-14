package com.tokopedia.core.gcm.services;

import android.app.IntentService;
import android.content.Intent;

import com.tokopedia.core.gcm.interactor.NotificationDataInteractor;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.gcm.model.FcmTokenUpdate;

import rx.Subscriber;

/**
 * @author by alvarisi on 12/8/16.
 */

public class NotificationIntentService extends IntentService {
    public static final String ARG_EXTRA_GCM_UPDATE = "ARG_EXTRA_GCM_UPDATE";
    public static final String ARG_EXTRA_GCM_UPDATE_DATA = "ARG_EXTRA_GCM_UPDATE_DATA";
    public static final int CODE_EXTRA_GCM_UPDATE = 1001;
    private NotificationDataInteractor mInteractor;

    public NotificationIntentService() {
        super(NotificationIntentService.class.getCanonicalName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int code = intent.getIntExtra(ARG_EXTRA_GCM_UPDATE, 0);
        switch (code) {
            case CODE_EXTRA_GCM_UPDATE:
                FcmTokenUpdate data = intent.getParcelableExtra(ARG_EXTRA_GCM_UPDATE_DATA);
                handleUpdateClientId(data);
                break;
        }
    }

    private void handleUpdateClientId(FcmTokenUpdate data) {
        if (mInteractor == null) mInteractor = new NotificationDataInteractor();
        mInteractor.updateTokenServer(data, new UpdateClientIdSubscriber());
    }

    private class UpdateClientIdSubscriber extends Subscriber<Boolean> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(Boolean s) {

        }
    }
}