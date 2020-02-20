package com.tokopedia.sellerhomedrawer.domain.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.tokopedia.sellerhomedrawer.data.drawernotification.NotificationModel;
import com.tokopedia.sellerhomedrawer.di.component.DaggerServiceComponent;
import com.tokopedia.sellerhomedrawer.di.component.ServiceComponent;
import com.tokopedia.sellerhomedrawer.di.module.BaseModule;
import com.tokopedia.sellerhomedrawer.domain.usecase.NewNotificationUseCase;
import com.tokopedia.sellerhomedrawer.domain.usecase.NotificationUseCase;

import javax.inject.Inject;

import rx.Subscriber;

public class SellerDrawerGetNotificationService extends JobIntentService {

    private static final int JOB_ID = 12383213;

    public static final String KEY_IS_SELLER = "is_seller";
    public static final String KEY_IS_REFRESH = "is_refresh";
    public static final String BROADCAST_GET_NOTIFICATION = "broadcast_get_notification";
    public static final String GET_NOTIFICATION_SUCCESS = "get_notification_success";
    public static final String UPDATE_NOTIFICATON_DATA = "update_notification_data";

    public static void startService(Context context, Boolean isRefresh) {
        Intent work = new Intent(context, SellerDrawerGetNotificationService.class);
        work.putExtra(KEY_IS_SELLER, true);
        work.putExtra(KEY_IS_REFRESH, isRefresh);
        enqueueWork(context, SellerDrawerGetNotificationService.class, JOB_ID, work);
    }

    public static void startService(Context context, Boolean isSeller, Boolean isRefresh) {
        Intent work = new Intent(context, SellerDrawerGetNotificationService.class);
        work.putExtra(KEY_IS_SELLER, isSeller);
        work.putExtra(KEY_IS_REFRESH, isRefresh);
        enqueueWork(context, SellerDrawerGetNotificationService.class, JOB_ID, work);
    }

    @Inject
    NewNotificationUseCase newNotificationUseCase;

    @Override
    public void onCreate() {
        super.onCreate();
        initInjector();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        boolean isSeller = intent.getBooleanExtra(KEY_IS_SELLER, false);
        boolean isRefresh = intent.getBooleanExtra(KEY_IS_REFRESH, false);

        newNotificationUseCase.setRefresh(isRefresh);
        newNotificationUseCase.execute(NotificationUseCase.getRequestParam(isSeller), new Subscriber<NotificationModel>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(NotificationModel notificationModel) {
                if (notificationModel.isSuccess())
                    sendBroadcast();
            }
        });
    }

    private void initInjector() {
        ServiceComponent serviceComponent = DaggerServiceComponent
                .builder()
                .baseModule(new BaseModule(getBaseContext()))
                .build();
        serviceComponent.inject(this);
    }

    public void sendBroadcast() {
        Intent intent = new Intent(BROADCAST_GET_NOTIFICATION);
        intent.putExtra(GET_NOTIFICATION_SUCCESS, true);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context) {
        Intent intent = new Intent(BROADCAST_GET_NOTIFICATION);
        intent.putExtra(GET_NOTIFICATION_SUCCESS, true);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
