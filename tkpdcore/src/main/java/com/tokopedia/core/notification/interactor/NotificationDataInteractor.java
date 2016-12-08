package com.tokopedia.core.notification.interactor;

import com.tokopedia.core.notification.interactor.source.CloudNotificationDataSource;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author  by alvarisi on 12/8/16.
 */

public class NotificationDataInteractor implements INotificationDataInteractor {
    private final CompositeSubscription mCompositeSubscription;
    private final CloudNotificationDataSource mCloudNotificationDataSource;

    public NotificationDataInteractor() {
        this.mCloudNotificationDataSource = new CloudNotificationDataSource();
        this.mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void updateClientFcmId(Map<String, String> param, Subscriber<String> subscriber) {
        this.mCloudNotificationDataSource.updateFcmClientId(param)
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
