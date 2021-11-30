package com.tokopedia.core.gcm.data;

import android.content.Context;

import com.tokopedia.core.gcm.data.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author  by alvarisi on 12/8/16.
 */

public class PushNotificationDataInteractor implements IPushNotificationDataInteractor {
    private final PushNotificationRepository mPushNotificationRepository;

    public PushNotificationDataInteractor(Context applicationContext) {
        this.mPushNotificationRepository = new PushNotificationDataRepository(applicationContext);
    }

    @Override
    public void updateTokenServer(FCMTokenUpdate data, Subscriber<FCMTokenUpdateEntity> subscriber) {
        this.mPushNotificationRepository.updateTokenServer(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber);
    }
}
