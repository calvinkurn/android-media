package com.tokopedia.core.gcm.data;

import com.tokopedia.core.gcm.data.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;

import rx.Subscriber;

/**
 * @author by alvarisi on 12/8/16.
 */

public interface IPushNotificationDataInteractor {
    void updateTokenServer(FCMTokenUpdate data, Subscriber<FCMTokenUpdateEntity> subscriber);

    void unSubscribeObservable();
}
