package com.tokopedia.core.notification.services;

import android.app.IntentService;
import android.content.Intent;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.notification.interactor.NotificationDataInteractor;
import com.tokopedia.core.notification.model.FcmClientIdUpdate;

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
                FcmClientIdUpdate data = intent.getParcelableExtra(ARG_EXTRA_GCM_UPDATE_DATA);
                handleUpdateClientId(data);
                break;
        }
    }

    private void handleUpdateClientId(FcmClientIdUpdate data) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("device_id_old", data.getOldClientId());
        param.put("device_id_new", data.getNewClientId());
        param.put("os_type", data.getOsType());
        if (mInteractor == null) mInteractor = new NotificationDataInteractor();
        mInteractor.updateClientFcmId(param, new UpdateClientIdSubscriber());
    }

    private class UpdateClientIdSubscriber extends Subscriber<String> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(String s) {

        }
    }
}