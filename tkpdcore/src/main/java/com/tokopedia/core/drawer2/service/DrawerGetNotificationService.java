package com.tokopedia.core.drawer2.service;

import android.content.Context;
import android.content.Intent;

import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.drawer2.data.pojo.notification.NotificationModel;
import com.tokopedia.core.drawer2.di.DaggerDrawerComponent;
import com.tokopedia.core.drawer2.domain.interactor.NewNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import rx.Subscriber;

public class DrawerGetNotificationService extends JobIntentService {

    private static final int JOB_ID = 12383213;
    public static final String BROADCAST_GET_NOTIFICATION = "broadcast_get_notification";
    public static final String GET_NOTIFICATION_SUCCESS = "get_notification_success";

    @Inject
    NewNotificationUseCase newNotificationUseCase;

    public static void startService(Context context, boolean isSeller) {
        Intent work = new Intent(context, DrawerGetNotificationService.class);
        work.putExtra("IS_SELLER", isSeller);
        enqueueWork(context, DrawerGetNotificationService.class, JOB_ID, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initInjector();
    }

    private void initInjector() {
        DaggerDrawerComponent.builder()
                .appComponent(MainApplication.getInstance().getApplicationComponent())
                .build()
                .inject(this);
    }

    @Override
    protected void onHandleWork(@NotNull Intent intent) {
        boolean isSeller = intent.getBooleanExtra("IS_SELLER", false);
        newNotificationUseCase.execute(NotificationUseCase.getRequestParam(isSeller), new Subscriber<NotificationModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(NotificationModel notificationModel) {
                if (notificationModel.isSuccess())
                    sendBroadcast();
            }


        });
    }

    private void sendBroadcast() {
        Intent intent = new Intent(BROADCAST_GET_NOTIFICATION);
        intent.putExtra(GET_NOTIFICATION_SUCCESS, true);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
