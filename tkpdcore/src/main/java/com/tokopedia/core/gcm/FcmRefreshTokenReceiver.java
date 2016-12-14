package com.tokopedia.core.gcm;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.gcm.model.FCMTokenUpdate;
import com.tokopedia.core.gcm.services.NotificationIntentService;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author by alvarisi on 12/13/16.
 */

public class FCMRefreshTokenReceiver implements IFCMRefreshTokenReceiver {
    private Context mContext;

    public FCMRefreshTokenReceiver(Context context) {
        this.mContext = context;
    }

    @Override
    public void onTokenReceive(Observable<FCMTokenUpdate> tokenUpdate) {
        tokenUpdate.subscribe(new Action1<FCMTokenUpdate>() {
            @Override
            public void call(FCMTokenUpdate FCMTokenUpdate) {
                Intent intent = new Intent(Intent.ACTION_SYNC, null, mContext,  NotificationIntentService.class);
                intent.putExtra(NotificationIntentService.ARG_EXTRA_GCM_UPDATE, NotificationIntentService.CODE_EXTRA_GCM_UPDATE);
                intent.putExtra(NotificationIntentService.ARG_EXTRA_GCM_UPDATE_DATA, FCMTokenUpdate);
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
