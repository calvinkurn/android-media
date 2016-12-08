package com.tokopedia.core.notification.interactor;

import java.util.Map;

import rx.Subscriber;

/**
 * @author by alvarisi on 12/8/16.
 */

public interface INotificationDataInteractor {
    void updateClientFcmId(Map<String, String> param, Subscriber<String> subscriber);

    void unSubscribeObservable();
}
