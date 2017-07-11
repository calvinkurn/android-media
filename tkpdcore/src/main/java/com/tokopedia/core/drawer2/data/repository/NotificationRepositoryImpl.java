package com.tokopedia.core.drawer2.data.repository;

import com.tokopedia.core.drawer2.data.factory.NotificationSourceFactory;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;

/**
 * Created by nisie on 5/5/17.
 */

public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationSourceFactory notificationSourceFactory;

    public NotificationRepositoryImpl(NotificationSourceFactory notificationSourceFactory) {
        this.notificationSourceFactory = notificationSourceFactory;
    }

    @Override
    public Observable<NotificationModel> getNotification(TKPDMapParam<String, Object> params) {
        return notificationSourceFactory.createCloudNotificationSource().getNotification(params);
    }
}
