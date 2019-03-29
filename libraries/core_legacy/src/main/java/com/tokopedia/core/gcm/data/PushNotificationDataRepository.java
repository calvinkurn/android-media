package com.tokopedia.core.gcm.data;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.data.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.domain.PushNotification;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.domain.model.DiscussionPushNotification;
import com.tokopedia.core.gcm.domain.model.MessagePushNotification;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by alvarisi on 1/5/17.
 */

public class PushNotificationDataRepository implements PushNotificationRepository {
    private final PushNotificationDataStoreFactory mPushNotificationDataStoreFactory;
    private final PushNotificationMapper mPushNotificationMapper;
    private Context context;

    public PushNotificationDataRepository(Context context) {
        this.context = context;
        mPushNotificationDataStoreFactory = new PushNotificationDataStoreFactory(context);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        mPushNotificationMapper = new PushNotificationMapper(gsonBuilder.create());
    }

    @Override
    public Observable<FCMTokenUpdateEntity> updateTokenServer(FCMTokenUpdate data) {
        return mPushNotificationDataStoreFactory
                .createCloudPushNotificationDataStore()
                .updateTokenServer(data);
    }

    @Override
    public Observable<DeviceRegistrationDataResponse> deviceRegistration() {
        return Observable
                .concat(
                        mPushNotificationDataStoreFactory
                                .createCloudPushNotificationDataStore()
                                .deviceRegistration(),
                        mPushNotificationDataStoreFactory
                                .createDiskPushNotificationDataStore()
                                .deviceRegistration()
                )
                .first(new Func1<DeviceRegistrationDataResponse, Boolean>() {
                    @Override
                    public Boolean call(DeviceRegistrationDataResponse response) {
                        return !TextUtils.isEmpty(response.getDeviceRegistration());
                    }
                });
    }

    @Override
    public Observable<Boolean> saveRegistrationDevice(String registration) {
        return mPushNotificationDataStoreFactory
                .createDiskPushNotificationDataStore()
                .saveRegistrationDevice(registration);
    }

    @Override
    public Observable<List<MessagePushNotification>> getSavedMessagePushNotification() {
        return mPushNotificationDataStoreFactory.createDiskPushNotificationDataStore()
                .getPushSavedPushNotificationWithOrderBy(Constants.ARG_NOTIFICATION_APPLINK_MESSAGE, true)
                .map(new Func1<List<PushNotification>, List<MessagePushNotification>>() {
                    @Override
                    public List<MessagePushNotification> call(List<PushNotification> pushNotifications) {
                        return mPushNotificationMapper.transformMessage(pushNotifications);
                    }
                });
    }

    @Override
    public Observable<String> storePushNotification(String category, String response, String customIndex, String serverId) {
        return mPushNotificationDataStoreFactory
                .createDiskPushNotificationDataStore()
                .savePushNotification(category, response, customIndex, serverId);
    }

    @Override
    public Observable<Boolean> storePushNotification(String category, String response, String customIndex) {
        return mPushNotificationDataStoreFactory
                .createDiskPushNotificationDataStore()
                .savePushNotification(category, response, customIndex);
    }

    @Override
    public Observable<Boolean> storePushNotification(String category, String response) {
        return mPushNotificationDataStoreFactory
                .createDiskPushNotificationDataStore()
                .savePushNotification(category, response);
    }

    @Override
    public Observable<List<DiscussionPushNotification>> getSavedDiscussionPushNotification() {
        return mPushNotificationDataStoreFactory.createDiskPushNotificationDataStore()
                .getPushSavedPushNotificationWithOrderBy(Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION, true)
                .map(new Func1<List<PushNotification>, List<DiscussionPushNotification>>() {
                    @Override
                    public List<DiscussionPushNotification> call(List<PushNotification> pushNotifications) {
                        return mPushNotificationMapper.transformDiscussion(pushNotifications);
                    }
                });
    }

    @Override
    public Observable<Boolean> clearPushNotificationStorage(String category) {
        return mPushNotificationDataStoreFactory.createDiskPushNotificationDataStore()
                .deleteSavedPushNotificationByCategory(category);
    }

    @Override
    public Observable<Boolean> clearPushNotificationStorage(String category, String serverId) {
        return mPushNotificationDataStoreFactory.createDiskPushNotificationDataStore()
                .deleteSavedPushNotificationByCategoryAndServerId(category, serverId);
    }

    @Override
    public Observable<Boolean> clearPushNotificationStorage() {
        return mPushNotificationDataStoreFactory.createDiskPushNotificationDataStore()
                .deleteSavedPushNotification();
    }
}
