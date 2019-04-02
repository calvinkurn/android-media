package com.tokopedia.core.gcm.domain.usecase;

import com.tokopedia.core.gcm.notification.applink.ApplinkVisitor;
import com.tokopedia.core.gcm.notification.applink.DiscussionPushNotificationWrapper;
import com.tokopedia.core.gcm.notification.applink.MessagePushNotificationWrapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by alvarisi on 2/23/17.
 */

public class GetSavedPushNotificationUseCase extends UseCase<List<ApplinkVisitor>> {

    private final GetSavedMessagePushNotificationUseCase messagePushNotificationUseCase;
    private final GetSavedDiscussionPushNotificationUseCase discussionPushNotificationUseCase;
    public GetSavedPushNotificationUseCase(GetSavedMessagePushNotificationUseCase messagePushNotificationUseCase,
                                           GetSavedDiscussionPushNotificationUseCase discussionPushNotificationUseCase) {
        this.messagePushNotificationUseCase = messagePushNotificationUseCase;
        this.discussionPushNotificationUseCase = discussionPushNotificationUseCase;
    }

    @Override
    public Observable<List<ApplinkVisitor>> createObservable(RequestParams requestParams) {
        return Observable.zip(messagePushNotificationUseCase.createObservable(RequestParams.EMPTY),
                discussionPushNotificationUseCase.createObservable(RequestParams.EMPTY),
                new Func2<MessagePushNotificationWrapper, DiscussionPushNotificationWrapper, List<ApplinkVisitor>>() {
                    @Override
                    public List<ApplinkVisitor> call(MessagePushNotificationWrapper messagePushNotificationWrapper, DiscussionPushNotificationWrapper discussionPushNotificationWrapper) {
                        List<ApplinkVisitor> applinkVisitors = new ArrayList<>();
                        applinkVisitors.add(messagePushNotificationWrapper);
                        applinkVisitors.add(discussionPushNotificationWrapper);
                        return applinkVisitors;
                    }
                });
    }
}
