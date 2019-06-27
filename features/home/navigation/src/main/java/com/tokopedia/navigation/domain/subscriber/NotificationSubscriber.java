package com.tokopedia.navigation.domain.subscriber;

import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.data.mapper.NotificationMapper;
import com.tokopedia.navigation.presentation.view.MainParentView;

import rx.Subscriber;

/**
 * Created by meta on 25/07/18.
 */
public class NotificationSubscriber extends Subscriber<NotificationEntity> {

    private MainParentView mainParentView;

    public NotificationSubscriber(MainParentView mainParentView) {
        this.mainParentView = mainParentView;
    }

    @Override
    public void onCompleted() { }

    @Override
    public void onError(Throwable e) { }

    @Override
    public void onNext(NotificationEntity notificationEntity) {
        if (notificationEntity != null)
            this.mainParentView.renderNotification(
                NotificationMapper.notificationMapper(
                        notificationEntity.getNotifications(),
                        notificationEntity.getNotifcenterUnread(),
                        notificationEntity.getFeed(),
                        notificationEntity.getHomeFlag()
                )
            );
    }
}
