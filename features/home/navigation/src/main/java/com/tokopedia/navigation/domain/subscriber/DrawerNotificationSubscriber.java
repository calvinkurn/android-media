package com.tokopedia.navigation.domain.subscriber;

import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.presentation.view.NotificationView;

import rx.Subscriber;

/**
 * Created by meta on 25/07/18.
 */
public class DrawerNotificationSubscriber extends Subscriber<NotificationEntity> {

    private NotificationView notificationView;

    public DrawerNotificationSubscriber(NotificationView notificationView) {
        this.notificationView = notificationView;
    }

    @Override
    public void onCompleted() {
        this.notificationView.onHideLoading();
    }

    @Override
    public void onError(Throwable e) {
        this.notificationView.onHideLoading();
        this.notificationView.onError(e.getMessage());
    }

    @Override
    public void onNext(NotificationEntity entity) {
        if (entity != null)
            this.notificationView.renderNotification(entity.getNotifications());
    }
}
