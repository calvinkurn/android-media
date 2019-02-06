package com.tokopedia.core.gcm.domain.usecase;

import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.domain.model.DiscussionPushNotification;
import com.tokopedia.core.gcm.notification.applink.DiscussionPushNotificationWrapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 2/23/17.
 */

public class GetSavedDiscussionPushNotificationUseCase extends UseCase<DiscussionPushNotificationWrapper> {
    private final PushNotificationRepository pushNotificationRepository;

    public GetSavedDiscussionPushNotificationUseCase(PushNotificationRepository pushNotificationRepository) {
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
