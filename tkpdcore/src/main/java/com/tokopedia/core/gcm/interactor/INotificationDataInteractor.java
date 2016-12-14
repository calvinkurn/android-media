package com.tokopedia.core.gcm.interactor;

import com.tokopedia.core.gcm.model.FCMTokenUpdate;

import rx.Subscriber;

/**
 * @author by alvarisi on 12/8/16.
 */

public interface INotificationDataInteractor {
    void updateTokenServer(FCMTokenUpdate data, Subscriber<Boolean> subscriber);

    void unSubscribeObservable();
}
