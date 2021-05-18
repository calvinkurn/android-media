package com.tokopedia.core.gcm.domain;

import com.tokopedia.core.gcm.data.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.domain.model.DiscussionPushNotification;
import com.tokopedia.core.gcm.domain.model.MessagePushNotification;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;

import java.util.List;

import rx.Observable;

/**
 * @author  by alvarisi on 1/5/17.
 */

public interface PushNotificationRepository {
    Observable<FCMTokenUpdateEntity> updateTokenServer(final FCMTokenUpdate data);

    Observable<DeviceRegistrationDataResponse> deviceRegistration();

    Observable<Boolean> saveRegistrationDevice(String registration);

    Observable<List<MessagePushNotification>> getSavedMessagePushNotification();

    Observable<List<DiscussionPushNotification>> getSavedDiscussionPushNotification();

    Observable<Boolean> clearPushNotificationStorage();
}
