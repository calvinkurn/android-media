package com.tokopedia.core.drawer2.view.subscriber;

import com.tokopedia.core.drawer2.data.viewmodel.TopChatNotificationModel;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;

import rx.Subscriber;

/**
 * @author by nisie on 11/17/17.
 */

public class TopChatNotificationSubscriber extends Subscriber<TopChatNotificationModel> {

    private final DrawerDataListener viewListener;

    public TopChatNotificationSubscriber(DrawerDataListener viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onErrorGetNotificationTopchat(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(TopChatNotificationModel topChatNotificationModel) {
        viewListener.onSuccessGetTopChatNotification(topChatNotificationModel.getNotifUnreads());
    }
}
