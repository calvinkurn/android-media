package com.tokopedia.core.gcm.interactor;

import com.tokopedia.core.gcm.model.FCMTokenUpdateData;

import rx.Subscriber;

/**
 * @author by alvarisi on 12/8/16.
 */

public interface IPushNotificationDataInteractor {
    void updateTokenServer(FCMTokenUpdateData data, Subscriber<Boolean> subscriber);

    void unSubscribeObservable();
}
