package com.tokopedia.core.gcm.data;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.data.entity.FCMTokenUpdateEntity;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.domain.model.DiscussionPushNotification;
import com.tokopedia.core.gcm.domain.model.MessagePushNotification;
import com.tokopedia.core.gcm.model.DeviceRegistrationDataResponse;
import com.tokopedia.core.gcm.model.FCMTokenUpdate;

import java.util.List;

import rx.Observable;
import timber.log.Timber;

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
        Timber.w("P2#PUSH_NOTIF_UNUSED#'updateTokenServer'");
        return mPushNotificationDataStoreFactory
                .createCloudPushNotificationDataStore()
                .updateTokenServer(data);
    }

    @Override
    public Observable<DeviceRegistrationDataResponse> deviceRegistration() {
        Timber.w("P2#PUSH_NOTIF_UNUSED#'deviceRegistration'");
        return Observable
                .concat(
                        mPushNotificationDataStoreFactory
                                .createCloudPushNotificationDataStore()
                                .deviceRegistration(),
                        mPushNotificationDataStoreFactory
                                .createDiskPushNotificationDataStore()
                                .deviceRegistration()
                )
                .first(response -> !TextUtils.isEmpty(response.getDeviceRegistration()));
    }

    @Override
    public Observable<Boolean> saveRegistrationDevice(String registration) {
        Timber.w("P2#PUSH_NOTIF_UNUSED#'saveRegistrationDevice'");
        return mPushNotificationDataStoreFactory
                .createDiskPushNotificationDataStore()
                .saveRegistrationDevice(registration);
    }

    @Override
    public Observable<List<MessagePushNotification>> getSavedMessagePushNotification() {
        Timber.w("P2#PUSH_NOTIF_UNUSED#'getSavedMessagePushNotification'");
        return mPushNotificationDataStoreFactory.createDiskPushNotificationDataStore()
                .getPushSavedPushNotificationWithOrderBy(Constants.ARG_NOTIFICATION_APPLINK_MESSAGE, true)
                .map(mPushNotificationMapper::transformMessage);
    }

    @Override
    public Observable<String> storePushNotification(String category, String response, String customIndex, String serverId) {
        Timber.w("P2#PUSH_NOTIF_UNUSED#'storePushNotification'");
        return mPushNotificationDataStoreFactory
                .createDiskPushNotificationDataStore()
                .savePushNotification(category, response, customIndex, serverId);
    }

    @Override
    public Observable<List<DiscussionPushNotification>> getSavedDiscussionPushNotification() {
        Timber.w("P2#PUSH_NOTIF_UNUSED#'getSavedDiscussionPushNotification'");
        return mPushNotificationDataStoreFactory.createDiskPushNotificationDataStore()
                .getPushSavedPushNotificationWithOrderBy(Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION, true)
                .map(mPushNotificationMapper::transformDiscussion);
    }

    @Override
    public Observable<Boolean> clearPushNotificationStorage() {
        return mPushNotificationDataStoreFactory.createDiskPushNotificationDataStore()
                .deleteSavedPushNotification();
    }
}
