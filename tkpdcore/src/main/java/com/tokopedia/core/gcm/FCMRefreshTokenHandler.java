package com.tokopedia.core.gcm;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.gcm.intentservices.PushNotificationIntentService;
import com.tokopedia.core.gcm.model.FCMTokenUpdateData;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by alvarisi on 12/13/16.
 */

public class FCMRefreshTokenHandler implements IFCMRefreshTokenHandler {
    private Context mContext;

    public FCMRefreshTokenHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public void onTokenReceive(Observable<FCMTokenUpdateData> tokenUpdate) {
        tokenUpdate.subscribe(new Action1<FCMTokenUpdateData>() {
            @Override
            public void call(FCMTokenUpdateData FCMTokenUpdateData) {
                Intent intent = new Intent(Intent.ACTION_SYNC, null, mContext,  PushNotificationIntentService.class);
                intent.putExtra(PushNotificationIntentService.ARG_EXTRA_GCM_UPDATE, PushNotificationIntentService.CODE_EXTRA_GCM_UPDATE);
                intent.putExtra(PushNotificationIntentService.ARG_EXTRA_GCM_UPDATE_DATA, FCMTokenUpdateData);
                mContext.startService(intent);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
