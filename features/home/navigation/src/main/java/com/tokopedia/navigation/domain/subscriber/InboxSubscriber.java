package com.tokopedia.navigation.domain.subscriber;

import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.exception.HttpErrorException;
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException;
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.presentation.view.InboxView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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
        handleErrorinitCartList(e);
    }

    @Override
    public void onNext(NotificationEntity notificationEntity) {
        if (notificationEntity != null)
            this.inboxView.onRenderNotifInbox(notificationEntity.getNotifications());
    }

    private void handleErrorinitCartList(Throwable e) {
        if (e instanceof UnknownHostException) {
            this.inboxView.onError(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
        } else if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
            this.inboxView.onError(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
        } else if (e instanceof ResponseErrorException) {
            this.inboxView.onError(e.getMessage());
        } else if (e instanceof ResponseDataNullException) {
            this.inboxView.onError(e.getMessage());
        } else if (e instanceof HttpErrorException) {
            this.inboxView.onError(e.getMessage());
        } else {
            this.inboxView.onError(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
    }
}
