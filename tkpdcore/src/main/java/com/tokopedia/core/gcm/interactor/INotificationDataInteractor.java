package com.tokopedia.core.gcm.interactor;

import com.tokopedia.core.gcm.model.FcmTokenUpdate;

import java.util.Map;

import rx.Subscriber;

/**
 * @author by alvarisi on 12/8/16.
 */

public interface INotificationDataInteractor {
    void updateTokenServer(FcmTokenUpdate data, Subscriber<String> subscriber);

    void unSubscribeObservable();
}
