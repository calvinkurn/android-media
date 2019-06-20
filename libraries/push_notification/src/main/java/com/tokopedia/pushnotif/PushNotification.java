package com.tokopedia.pushnotif;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.PushNotificationGeneratedDatabaseHolder;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.pushnotif.domain.pojo.TrackPushNotificationEntity;
import com.tokopedia.pushnotif.domain.usecase.TrackPushNotificationUseCase;
import com.tokopedia.pushnotif.factory.ChatNotificationFactory;
import com.tokopedia.pushnotif.factory.GeneralNotificationFactory;
import com.tokopedia.pushnotif.factory.SummaryNotificationFactory;
import com.tokopedia.pushnotif.factory.TalkNotificationFactory;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;
import com.tokopedia.usecase.RequestParams;

import rx.Subscriber;

/**
 * @author ricoharisin .
 */

public class PushNotification {

    public static void initDatabase(Context applicationContext) {
        try{
            FlowManager.getConfig();
        } catch (IllegalStateException e) {
            FlowManager.init(new FlowConfig.Builder(applicationContext).build());
        }
        FlowManager.initModule(PushNotificationGeneratedDatabaseHolder.class);
    }

    public static void notify(Context context, Bundle data) {
        ApplinkNotificationModel applinkNotificationModel = ApplinkNotificationHelper.convertToApplinkModel(data);

        if (ApplinkNotificationHelper.allowToShow(context, applinkNotificationModel)) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            int notificationId = ApplinkNotificationHelper.generateNotifictionId(applinkNotificationModel.getApplinks());

            if (notificationId == Constant.NotificationId.TALK) {
                notifyTalk(context, applinkNotificationModel, notificationId, notificationManagerCompat);
            } else if (notificationId == Constant.NotificationId.CHAT) {
                notifyChat(context, applinkNotificationModel, notificationId, notificationManagerCompat);
            } else if (notificationId == Constant.NotificationId.GROUPCHAT) {
                notifyGroupChat(context, applinkNotificationModel, notificationId, notificationManagerCompat);
            } else {
                notifyGeneral(context, applinkNotificationModel, notificationId, notificationManagerCompat);
            }
            trackDeliveredNotification(context, applinkNotificationModel);
        }
    }

    private static void trackDeliveredNotification(Context context, ApplinkNotificationModel applinkNotificationModel) {
        GraphqlUseCase gqlUseCase = new GraphqlUseCase();
        TrackPushNotificationUseCase useCase = new TrackPushNotificationUseCase(context, gqlUseCase);
        RequestParams requestParams = useCase.createRequestParam(applinkNotificationModel);

        useCase.createObservable(requestParams)
                .take(1)
                .subscribe(new Subscriber<TrackPushNotificationEntity>() {
                    @Override
                    public void onCompleted() {
                        Log.d("TEST_TRACK", "tracked");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TEST_TRACK", "tracked");
                    }

                    @Override
                    public void onNext(TrackPushNotificationEntity trackPushNotificationEntity) {
                        Log.d("TEST_TRACK", "tracked");
                    }
                });
    }

    private static void notifyTalk(Context context, ApplinkNotificationModel applinkNotificationModel,
                                   int notificationType, NotificationManagerCompat notificationManagerCompat) {

        int notificationId = ApplinkNotificationHelper.getNotificationId(applinkNotificationModel.getApplinks());

        Notification notifTalk = new TalkNotificationFactory(context)
                    .createNotification(applinkNotificationModel, notificationType, notificationId);

        SummaryNotificationFactory summaryNotificationFactory = new SummaryNotificationFactory(context);
        Notification notifSummary = summaryNotificationFactory
                .createNotification(applinkNotificationModel, notificationType, notificationType);

        if (ApplinkNotificationHelper.allowGroup())
            notificationManagerCompat.notify(notificationId, notifTalk);

        if ((ApplinkNotificationHelper.allowGroup() && summaryNotificationFactory.getTotalSummary() > 1)
                || (!ApplinkNotificationHelper.allowGroup() && summaryNotificationFactory.getTotalSummary() >= 1)) {
            notificationManagerCompat.notify(notificationType, notifSummary);
        }

    }

    private static void notifyChat(Context context, ApplinkNotificationModel applinkNotificationModel,
                                   int notificationType, NotificationManagerCompat notificationManagerCompat) {

        int notificationId = ApplinkNotificationHelper.getNotificationId(applinkNotificationModel.getApplinks());

        Notification notifChat = new ChatNotificationFactory(context)
                .createNotification(applinkNotificationModel, notificationType, notificationId);

        SummaryNotificationFactory summaryNotificationFactory = new SummaryNotificationFactory(context);
        Notification notifSummary = summaryNotificationFactory
                .createNotification(applinkNotificationModel, notificationType, notificationType);

        if (ApplinkNotificationHelper.allowGroup())
            notificationManagerCompat.notify(notificationId, notifChat);

        if ((ApplinkNotificationHelper.allowGroup() && summaryNotificationFactory.getTotalSummary() > 1)
                || (!ApplinkNotificationHelper.allowGroup() && summaryNotificationFactory.getTotalSummary() >= 1)) {
            notificationManagerCompat.notify(notificationType, notifSummary);
        }

    }

    private static void notifyGroupChat(Context context, ApplinkNotificationModel applinkNotificationModel,
                                        int notificationType, NotificationManagerCompat notificationManagerCompat) {

        Notification notifChat = new GeneralNotificationFactory(context)
                .createNotification(applinkNotificationModel, notificationType, notificationType);

        notificationManagerCompat.notify(notificationType, notifChat);

    }

    private static void notifyGeneral(Context context, ApplinkNotificationModel applinkNotificationModel,
                                   int notificationType, NotificationManagerCompat notificationManagerCompat) {
        Notification notifChat = new GeneralNotificationFactory(context)
                .createNotification(applinkNotificationModel, notificationType, notificationType);

        notificationManagerCompat.notify(notificationType, notifChat);

    }

    private static void notifyChallenges(Context context, ApplinkNotificationModel applinkNotificationModel,
                                      int notificationType, NotificationManagerCompat notificationManagerCompat) {
        Notification notifChat = new GeneralNotificationFactory(context)
                .createNotification(applinkNotificationModel, notificationType, notificationType);

        notificationManagerCompat.notify(notificationType, notifChat);
    }
}
