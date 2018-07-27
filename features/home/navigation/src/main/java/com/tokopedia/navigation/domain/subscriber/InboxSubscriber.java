package com.tokopedia.navigation.domain.subscriber;

import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.data.mapper.NotificationMapper;
import com.tokopedia.navigation.presentation.view.InboxView;

import rx.Subscriber;

/**
 * Created by meta on 25/07/18.
 */
public class InboxSubscriber extends Subscriber<NotificationEntity> {

    private InboxView inboxView;

    public InboxSubscriber(InboxView inboxView) {
        this.inboxView = inboxView;
    }

    @Override
    public void onCompleted() {
        this.inboxView.onHideLoading();
    }

    @Override
    public void onError(Throwable e) {
        this.inboxView.onHideLoading();
        this.inboxView.onError(e.getMessage());
    }

    @Override
    public void onNext(NotificationEntity notificationEntity) {
        if (notificationEntity != null)
            this.inboxView.onRenderNotifINbox(notificationEntity.getNotifications());
    }
}
