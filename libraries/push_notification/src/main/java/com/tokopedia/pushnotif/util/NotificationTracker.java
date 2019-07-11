package com.tokopedia.pushnotif.util;

import android.content.Context;

import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.pushnotif.domain.pojo.TrackPushNotificationEntity;
import com.tokopedia.pushnotif.domain.usecase.TrackPushNotificationUseCase;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;
import com.tokopedia.usecase.RequestParams;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;

public class NotificationTracker {

    private static NotificationTracker instance;

    private final Context context;
    private final GraphqlUseCase gqlUseCase;
    private final TrackPushNotificationUseCase useCase;

    private NotificationTracker(Context context) {
        this.context = context;
        this.gqlUseCase = new GraphqlUseCase();
        this.useCase = new TrackPushNotificationUseCase(this.context, this.gqlUseCase);
    }

    public static NotificationTracker getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationTracker(context.getApplicationContext());
        }
        return instance;
    }

    public void trackDeliveredNotification(ApplinkNotificationModel applinkNotificationModel) {
        RequestParams requestParams = useCase.createRequestParam(applinkNotificationModel);

        useCase.createObservable(requestParams)
                .take(1)
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(new Subscriber<TrackPushNotificationEntity>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(TrackPushNotificationEntity trackPushNotificationEntity) {
                    }
                });
    }
}
