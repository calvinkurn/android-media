package com.tokopedia.tkpd.fcm.applink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationConfiguration;
import com.tokopedia.core.gcm.data.PushNotificationDataRepository;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.domain.usecase.GetSavedDiscussionPushNotificationUseCase;
import com.tokopedia.core.gcm.domain.usecase.GetSavedMessagePushNotificationUseCase;
import com.tokopedia.core.gcm.domain.usecase.GetSavedPushNotificationUseCase;
import com.tokopedia.core.gcm.model.ApplinkNotificationPass;
import com.tokopedia.core.gcm.notification.applink.ApplinkPushNotificationBuildAndShow;
import com.tokopedia.core.gcm.notification.applink.ApplinkTypeFactory;
import com.tokopedia.core.gcm.notification.applink.ApplinkTypeFactoryList;
import com.tokopedia.core.gcm.notification.applink.DiscussionPushNotificationWrapper;
import com.tokopedia.core.gcm.notification.applink.MessagePushNotificationWrapper;
import com.tokopedia.core.gcm.notification.applink.PromoPushNotificationBuildAndShow;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.usecase.RequestParams;

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
        PushNotificationRepository pushNotificationRepository = new PushNotificationDataRepository(context);
        getSavedMessagePushNotificationUseCase =
                new GetSavedMessagePushNotificationUseCase(
                        pushNotificationRepository
                );

        getSavedDiscussionPushNotificationUseCase =
                new GetSavedDiscussionPushNotificationUseCase(
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

    public void showPromoNotification(Bundle data){

        Intent intent = new Intent(context, DeeplinkHandlerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PromoPushNotificationBuildAndShow promoPushNotificationBuildAndShow = new PromoPushNotificationBuildAndShow(data);
        promoPushNotificationBuildAndShow.process(context, intent);

    }

    /**
     * should not use this function again for performance reason.
     * There is no need to create this ApplinkBuildAndShowNotification, only for call this function
     * as this function only use the "context".
     * Thus, no no need to create PushNotificationRepository, getSavedMessagePushNotificationUseCase, and other objects
     */
    @Deprecated
    public void showApplinkNotification(Bundle data){
        showApplinkNotification(context, data);
    }

    public static void showApplinkNotification(Context context, Bundle data) {
        ApplinkPushNotificationBuildAndShow buildAndShow = new ApplinkPushNotificationBuildAndShow(data);
        Intent intent = new Intent(context, DeeplinkHandlerActivity.class);
        buildAndShow.process(context, intent);
    }
}
