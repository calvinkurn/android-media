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
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author by alvarisi on 1/5/17.
 */

public class PushNotificationDataRepository implements PushNotificationRepository {
    private final PushNotificationDataStoreFactory mPushNotificationDataStoreFactory;
    private final PushNotificationMapper mPushNotificationMapper;
    private final Context context;

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
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "updateTokenServer");
        ServerLogger.log(Priority.P2, "PUSH_NOTIF_UNUSED", messageMap);
        return mPushNotificationDataStoreFactory
                .createCloudPushNotificationDataStore()
                .updateTokenServer(data);
    }

    @Override
    public Observable<DeviceRegistrationDataResponse> deviceRegistration() {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "deviceRegistration");
        ServerLogger.log(Priority.P2, "PUSH_NOTIF_UNUSED", messageMap);
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
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "saveRegistrationDevice");
        ServerLogger.log(Priority.P2, "PUSH_NOTIF_UNUSED", messageMap);
        return mPushNotificationDataStoreFactory
                .createDiskPushNotificationDataStore()
                .saveRegistrationDevice(registration);
    }

    @Override
    public Observable<List<MessagePushNotification>> getSavedMessagePushNotification() {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "getSavedMessagePushNotification");
        ServerLogger.log(Priority.P2, "PUSH_NOTIF_UNUSED", messageMap);
        return mPushNotificationDataStoreFactory.createDiskPushNotificationDataStore()
                .getPushSavedPushNotificationWithOrderBy(Constants.ARG_NOTIFICATION_APPLINK_MESSAGE, true)
                .map(mPushNotificationMapper::transformMessage);
    }

    @Override
    public Observable<String> storePushNotification(String category, String response, String customIndex, String serverId) {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "storePushNotification");
        ServerLogger.log(Priority.P2, "PUSH_NOTIF_UNUSED", messageMap);
        return mPushNotificationDataStoreFactory
                .createDiskPushNotificationDataStore()
                .savePushNotification(category, response, customIndex, serverId);
    }

    @Override
    public Observable<List<DiscussionPushNotification>> getSavedDiscussionPushNotification() {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "getSavedDiscussionPushNotification");
        ServerLogger.log(Priority.P2, "PUSH_NOTIF_UNUSED", messageMap);
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
