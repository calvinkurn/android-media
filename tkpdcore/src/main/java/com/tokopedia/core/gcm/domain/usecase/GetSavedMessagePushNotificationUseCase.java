package com.tokopedia.core.gcm.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.domain.model.MessagePushNotification;
import com.tokopedia.core.gcm.notification.applink.MessagePushNotificationWrapper;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 2/23/17.
 */

public class GetSavedMessagePushNotificationUseCase extends UseCase<MessagePushNotificationWrapper> {
    private final PushNotificationRepository pushNotificationRepository;

    public GetSavedMessagePushNotificationUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, PushNotificationRepository pushNotificationRepository) {
        super(threadExecutor, postExecutionThread);
        this.pushNotificationRepository = pushNotificationRepository;
    }

    @Override
    public Observable<MessagePushNotificationWrapper> createObservable(RequestParams requestParams) {
        return pushNotificationRepository.getSavedMessagePushNotification().map(new Func1<List<MessagePushNotification>, MessagePushNotificationWrapper>() {
            @Override
            public MessagePushNotificationWrapper call(List<MessagePushNotification> messagePushNotifications) {
                MessagePushNotificationWrapper messagePushNotificationWrapper = new MessagePushNotificationWrapper();
                messagePushNotificationWrapper.setMessagePushNotifications(messagePushNotifications);
                return messagePushNotificationWrapper;
            }
        });
    }
}