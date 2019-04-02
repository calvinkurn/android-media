package com.tokopedia.core.gcm.data;

import android.content.Context;

import com.tokopedia.core.gcm.data.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author  by alvarisi on 12/8/16.
 */

public class PushNotificationDataInteractor implements IPushNotificationDataInteractor {
    private final CompositeSubscription mCompositeSubscription;
    private final PushNotificationRepository mPushNotificationRepository;
    private Context applicationContext;

    public PushNotificationDataInteractor(Context applicationContext) {
        this.applicationContext = applicationContext;
        this.mCompositeSubscription = new CompositeSubscription();
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

    @Override
    public void unSubscribeObservable() {
        if (mCompositeSubscription.hasSubscriptions()) mCompositeSubscription.unsubscribe();
    }
}
