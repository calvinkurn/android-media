package com.tokopedia.core.drawer2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationData;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.data.viewmodel.ChatNotificationModel;

import rx.Observable;
import rx.functions.Func2;

/**
 * @author by nisie on 11/23/17.
 */

public class NewNotificationUseCase extends UseCase<NotificationModel> {

    NotificationUseCase notificationUseCase;
    GetChatNotificationUseCase getChatNotificationUseCase;

    public NewNotificationUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  NotificationUseCase notificationUseCase,
                                  GetChatNotificationUseCase getChatNotificationUseCase) {
        super(threadExecutor, postExecutionThread);
        this.notificationUseCase = notificationUseCase;
        this.getChatNotificationUseCase = getChatNotificationUseCase;
    }

    @Override
    public Observable<NotificationModel> createObservable(RequestParams requestParams) {
        Observable<NotificationModel> notif = notificationUseCase.createObservable(requestParams);
        Observable<ChatNotificationModel> notifTopChat = getChatNotificationUseCase.createObservable(com.tokopedia.usecase.RequestParams.EMPTY);

        return Observable.zip(notif, notifTopChat, new Func2<NotificationModel, ChatNotificationModel, NotificationModel>() {
            @Override
            public NotificationModel call(NotificationModel notificationModel, ChatNotificationModel chatNotificationModel) {
                NotificationData data = notificationModel.getNotificationData();
                data.setTotalNotif(data.getTotalNotif() - data.getInbox().getInboxMessage() +
                        chatNotificationModel.getNotifUnreads());
                data.getInbox().setInboxMessage(chatNotificationModel.getNotifUnreads());
                notificationModel.setNotificationData(
                        data
                );
                return notificationModel;
            }
        });

    }
}
