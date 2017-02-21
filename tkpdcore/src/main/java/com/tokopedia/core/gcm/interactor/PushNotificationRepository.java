package com.tokopedia.core.gcm.interactor;

import com.tokopedia.core.gcm.interactor.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;

import rx.Observable;

/**
 * @author  by alvarisi on 1/5/17.
 */

public interface PushNotificationRepository {
    Observable<FCMTokenUpdateEntity> updateTokenServer(final FCMTokenUpdate data);

    Observable<DeviceRegistrationDataResponse> deviceRegistration();

    Observable<Boolean> saveRegistrationDevice(String registration);
}
