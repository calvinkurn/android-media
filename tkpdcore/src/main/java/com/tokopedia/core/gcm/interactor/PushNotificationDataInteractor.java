package com.tokopedia.core.gcm.interactor;

import com.tokopedia.core.gcm.interactor.entity.FCMTokenUpdateEntity;
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

    public PushNotificationDataInteractor() {
        this.mCompositeSubscription = new CompositeSubscription();
        this.mPushNotificationRepository = new PushNotificationDataRepository();
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
