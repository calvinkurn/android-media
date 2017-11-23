package com.tokopedia.core.drawer2.data.repository;

import com.tokopedia.core.drawer2.data.factory.NotificationSourceFactory;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.data.source.TopChatNotificationSource;
import com.tokopedia.core.drawer2.data.viewmodel.TopChatNotificationModel;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationSourceFactory notificationSourceFactory;
    private TopChatNotificationSource topChatNotificationSource;

    public NotificationRepositoryImpl(NotificationSourceFactory notificationSourceFactory,
                                      TopChatNotificationSource topChatNotificationSource) {
        this.notificationSourceFactory = notificationSourceFactory;
        this.topChatNotificationSource = topChatNotificationSource;
    }

    @Override
    public Observable<NotificationModel> getNotification(TKPDMapParam<String, Object> params) {
        return notificationSourceFactory.createCloudNotificationSource().getNotification(params);
    }

    @Override
    public Observable<TopChatNotificationModel> getNotificationTopChat(TKPDMapParam<String, Object>
                                                                           params) {
        return topChatNotificationSource.getNotificationTopChat(params);
    }
}
