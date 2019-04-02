package com.tokopedia.core.drawer2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationData;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.data.viewmodel.TopChatNotificationModel;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author by nisie on 11/23/17.
 */

public class NewNotificationUseCase extends UseCase<NotificationModel> {

    NotificationUseCase notificationUseCase;
    TopChatNotificationUseCase topChatNotificationUseCase;

    public NewNotificationUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  NotificationUseCase notificationUseCase,
                                  TopChatNotificationUseCase topChatNotificationUseCase) {
        super(threadExecutor, postExecutionThread);
        this.notificationUseCase = notificationUseCase;
        this.topChatNotificationUseCase = topChatNotificationUseCase;
    }

    @Override
    public Observable<NotificationModel> createObservable(RequestParams requestParams) {
        Observable<NotificationModel> notif = notificationUseCase.createObservable(requestParams);
        Observable<TopChatNotificationModel> notifTopChat = topChatNotificationUseCase.createObservable
                (requestParams);

        return Observable.zip(notif, notifTopChat, new Func2<NotificationModel, TopChatNotificationModel, NotificationModel>() {
            @Override
            public NotificationModel call(NotificationModel notificationModel, TopChatNotificationModel topChatNotificationModel) {
                NotificationData data = notificationModel.getNotificationData();
                data.setTotalNotif(data.getTotalNotif() - data.getInbox().getInboxMessage() +
                        topChatNotificationModel.getNotifUnreads());
                data.getInbox().setInboxMessage(topChatNotificationModel.getNotifUnreads());
                notificationModel.setNotificationData(
                        data
                );
                return notificationModel;
            }
        });

    }
}
