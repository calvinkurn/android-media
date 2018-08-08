package com.tokopedia.core.gcm.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.domain.model.DiscussionPushNotification;
import com.tokopedia.core.gcm.notification.applink.DiscussionPushNotificationWrapper;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 2/23/17.
 */

public class GetSavedDiscussionPushNotificationUseCase extends UseCase<DiscussionPushNotificationWrapper> {
    private final PushNotificationRepository pushNotificationRepository;
    public GetSavedDiscussionPushNotificationUseCase(ThreadExecutor threadExecutor,
                                                     PostExecutionThread postExecutionThread,
                                                     PushNotificationRepository pushNotificationRepository) {
        super(threadExecutor, postExecutionThread);
        this.pushNotificationRepository = pushNotificationRepository;
    }

    @Override
    public Observable<DiscussionPushNotificationWrapper> createObservable(RequestParams requestParams) {
        return pushNotificationRepository.getSavedDiscussionPushNotification().map(new Func1<List<DiscussionPushNotification>, DiscussionPushNotificationWrapper>() {
            @Override
            public DiscussionPushNotificationWrapper call(List<DiscussionPushNotification> discussionPushNotifications) {
                DiscussionPushNotificationWrapper discussionPushNotificationWrapper = new DiscussionPushNotificationWrapper();
                discussionPushNotificationWrapper.setDiscussionPushNotifications(discussionPushNotifications);
                return discussionPushNotificationWrapper;
            }
        });
    }
}
