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
    public static final String UPDATE_NOTIFICATION_DATA = "update_notification_data";
    public static final String BROADCAST_GET_NOTIFICATION = "broadcast_get_notification";
    public static final String GET_NOTIFICATION_SUCCESS = "get_notification_success";
    private static final String KEY_IS_SELLER = "is_seller";
    private static final String KEY_IS_REFRESH = "is_refresh";

    @Inject
    NewNotificationUseCase newNotificationUseCase;

    public static void startService(Context context, boolean isSeller, boolean isRefresh) {
        Intent work = new Intent(context, DrawerGetNotificationService.class);
        work.putExtra(KEY_IS_SELLER, isSeller);
        work.putExtra(KEY_IS_REFRESH, isRefresh);
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
        boolean isSeller = intent.getBooleanExtra(KEY_IS_SELLER, false);
        boolean isRefresh = intent.getBooleanExtra(KEY_IS_REFRESH, false);

        newNotificationUseCase.setRefresh(isRefresh);
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
