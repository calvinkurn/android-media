package com.tokopedia.tkpd.fcm;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.gcm.data.PushNotificationDataRepository;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.domain.usecase.GetSavedDiscussionPushNotificationUseCase;
import com.tokopedia.core.gcm.domain.usecase.GetSavedMessagePushNotificationUseCase;
import com.tokopedia.core.gcm.domain.usecase.GetSavedPushNotificationUseCase;
import com.tokopedia.core.gcm.notification.applink.ApplinkTypeFactory;
import com.tokopedia.core.gcm.notification.applink.ApplinkTypeFactoryList;
import com.tokopedia.core.gcm.notification.applink.ApplinkVisitor;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;

import java.util.List;

import rx.Subscriber;

/**
 * Created by alvarisi on 3/6/17.
 */

public class ApplinkBuildAndShowNotification {
    private GetSavedPushNotificationUseCase getSavedPushNotificationUseCase;
    private final Context context;
    public ApplinkBuildAndShowNotification(Context context) {
        this.context = context;
        PushNotificationRepository pushNotificationRepository = new PushNotificationDataRepository();
        GetSavedMessagePushNotificationUseCase getSavedMessagePushNotificationUseCase =
                new GetSavedMessagePushNotificationUseCase(
                        new JobExecutor(),
                        new UIThread(),
                        pushNotificationRepository
                );

        GetSavedDiscussionPushNotificationUseCase getSavedDiscussionPushNotificationUseCase =
                new GetSavedDiscussionPushNotificationUseCase(
                        new JobExecutor(),
                        new UIThread(),
                        pushNotificationRepository
                );

        getSavedPushNotificationUseCase = new GetSavedPushNotificationUseCase(
                new JobExecutor(),
                new UIThread(),
                getSavedMessagePushNotificationUseCase,
                getSavedDiscussionPushNotificationUseCase
        );
    }

    public void show(){
        getSavedPushNotificationUseCase.execute(RequestParams.EMPTY, new Subscriber<List<ApplinkVisitor>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<ApplinkVisitor> applinkVisitors) {
                Intent intent = new Intent(context, DeeplinkHandlerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ApplinkTypeFactory applinkTypeFactory = new ApplinkTypeFactoryList();
                for (ApplinkVisitor applinkVisitor : applinkVisitors){
                    applinkVisitor.type(applinkTypeFactory).process(context, intent);
                }
            }
        });
    }
}
