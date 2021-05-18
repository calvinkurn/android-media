package com.tokopedia.core.gcm.data;

import com.tokopedia.core.gcm.data.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.domain.PushNotification;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 2/21/17.
 */

public interface PushNotificationDataStore {
    Observable<FCMTokenUpdateEntity> updateTokenServer(FCMTokenUpdate data);

    Observable<DeviceRegistrationDataResponse> deviceRegistration();

    Observable<Boolean> saveRegistrationDevice(String registrationDevice);

    Observable<List<PushNotification>> getPushSavedPushNotificationWithOrderBy(String category, boolean ascendant);

    Observable<Boolean> deleteSavedPushNotification();
}
