package com.tokopedia.navigation.domain.subscriber;

import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException;
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.data.mapper.NotificationRequestMapper;
import com.tokopedia.navigation.presentation.view.NotificationView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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
        handleErrorinitCartList(e);
    }

    @Override
    public void onNext(NotificationEntity entity) {
        if (entity != null) {
            this.notificationView.renderNotification(entity.getNotifications(),
                    entity.getNotifcenterUnread(),
                    NotificationRequestMapper.isHasShop(entity));
        }
    }

    private void handleErrorinitCartList(Throwable e) {
        if (e instanceof UnknownHostException) {
            this.notificationView.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
        } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            this.notificationView.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof ResponseErrorException) {
            this.notificationView.onError(e.getMessage());
        } else if (e instanceof ResponseDataNullException) {
            this.notificationView.onError(e.getMessage());
        } else if (e instanceof HttpErrorException) {
            this.notificationView.onError(e.getMessage());
        } else {
            this.notificationView.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }
}
