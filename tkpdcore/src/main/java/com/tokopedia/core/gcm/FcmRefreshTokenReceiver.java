package com.tokopedia.core.gcm;

import android.app.Application;
import android.content.Intent;

import com.tokopedia.core.gcm.model.FcmTokenUpdate;
import com.tokopedia.core.gcm.services.NotificationIntentService;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by alvarisi on 12/13/16.
 */

public class FcmRefreshTokenReceiver implements IFcmRefreshTokenReceiver {
    private Application mApplication;

    public FcmRefreshTokenReceiver(Application application) {
        this.mApplication = application;
    }

    @Override
    public void onTokenReceive(Observable<FcmTokenUpdate> tokenUpdate) {
        tokenUpdate.subscribe(new Action1<FcmTokenUpdate>() {
            @Override
            public void call(FcmTokenUpdate fcmTokenUpdate) {
                Intent intent = new Intent(mApplication, NotificationIntentService.class);
                intent.putExtra(NotificationIntentService.ARG_EXTRA_GCM_UPDATE, NotificationIntentService.CODE_EXTRA_GCM_UPDATE);
                intent.putExtra(NotificationIntentService.ARG_EXTRA_GCM_UPDATE_DATA, fcmTokenUpdate);
                mApplication.startService(intent);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
