package com.tokopedia.tkpd.fcm;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.data.PushNotificationDataRepository;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.domain.usecase.GetSavedDiscussionPushNotificationUseCase;
import com.tokopedia.core.gcm.domain.usecase.GetSavedMessagePushNotificationUseCase;
import com.tokopedia.core.gcm.domain.usecase.GetSavedPushNotificationUseCase;
import com.tokopedia.core.gcm.notification.applink.ApplinkTypeFactory;
import com.tokopedia.core.gcm.notification.applink.ApplinkTypeFactoryList;
import com.tokopedia.core.gcm.notification.applink.DiscussionPushNotificationWrapper;
import com.tokopedia.core.gcm.notification.applink.MessagePushNotificationWrapper;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;

import rx.Subscriber;

/**
 * Created by alvarisi on 3/6/17.
 */

public class ApplinkBuildAndShowNotification {
    private GetSavedMessagePushNotificationUseCase getSavedMessagePushNotificationUseCase;
    private GetSavedDiscussionPushNotificationUseCase getSavedDiscussionPushNotificationUseCase;
    private final Context context;

    public ApplinkBuildAndShowNotification(Context context) {
        this.context = context;
        PushNotificationRepository pushNotificationRepository = new PushNotificationDataRepository();
        getSavedMessagePushNotificationUseCase =
                new GetSavedMessagePushNotificationUseCase(
                        new JobExecutor(),
                        new UIThread(),
                        pushNotificationRepository
                );

        getSavedDiscussionPushNotificationUseCase =
                new GetSavedDiscussionPushNotificationUseCase(
                        new JobExecutor(),
                        new UIThread(),
                        pushNotificationRepository
                );
    }

    public void show(String category) {
        switch (category) {
            case Constants.ARG_NOTIFICATION_APPLINK_MESSAGE:
                showMessagePersonalizedNotification(true);
                break;
            case Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION:
                showDiscussionPersonalizedNotification(true);
                break;
        }
    }

    public void showMessagePersonalizedNotification(final Boolean isNew) {
        getSavedMessagePushNotificationUseCase.execute(RequestParams.EMPTY, new Subscriber<MessagePushNotificationWrapper>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(MessagePushNotificationWrapper messagePushNotificationWrapper) {
                Intent intent = new Intent(context, DeeplinkHandlerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

                ApplinkTypeFactory applinkTypeFactory = new ApplinkTypeFactoryList();
                messagePushNotificationWrapper.type(applinkTypeFactory).process(context, intent, isNew);
            }
        });
    }


    public void showDiscussionPersonalizedNotification(final Boolean isNew) {
        getSavedDiscussionPushNotificationUseCase.execute(RequestParams.EMPTY, new Subscriber<DiscussionPushNotificationWrapper>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(DiscussionPushNotificationWrapper discussionPushNotificationWrapper) {
                Intent intent = new Intent(context, DeeplinkHandlerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);

                ApplinkTypeFactory applinkTypeFactory = new ApplinkTypeFactoryList();
                discussionPushNotificationWrapper.type(applinkTypeFactory).process(context, intent, isNew);
            }
        });
    }
}
