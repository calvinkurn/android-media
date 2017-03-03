package com.tokopedia.core.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.gcm.data.PushNotificationDataRepository;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.domain.usecase.DeleteSavedPushNotificationByCategoryAndServerIdUseCase;
import com.tokopedia.core.gcm.domain.usecase.DeleteSavedPushNotificationByCategoryUseCase;
import com.tokopedia.core.gcm.domain.usecase.DeleteSavedPushNotificationUseCase;
import com.tokopedia.core.var.TkpdCache;

import rx.Subscriber;

public class NotificationModHandler {

    private Context mContext;
    private NotificationManager mNotificationManager;
    
    public NotificationModHandler(Context context, int code) {
    	mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	this.mContext = context;
    }

    public NotificationModHandler(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.mContext = context;
    }

    public static void clearCacheIfFromNotification(Context context, Intent intent) {
        if (intent != null && intent.hasExtra(Constants.EXTRA_FROM_PUSH)){
            if (intent.getBooleanExtra(Constants.EXTRA_FROM_PUSH, false)){
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(100);
                LocalCacheHandler.clearCache(context, TkpdCache.GCM_NOTIFICATION);
            }
        }
    }

    public static void clearCacheAllNotification(){
        PushNotificationRepository pushNotificationRepository = new PushNotificationDataRepository();
        DeleteSavedPushNotificationUseCase deleteUseCase =
                new DeleteSavedPushNotificationUseCase(
                        new JobExecutor(),
                        new UIThread(),
                        pushNotificationRepository
                );
        deleteUseCase.execute(RequestParams.EMPTY, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean)
                    CommonUtils.dumper("Success Clear Storage Notification");
                else
                    CommonUtils.dumper("Failed Clear Storage Notification");
            }
        });
    }

    public static void clearCacheIfFromNotification(String category){
        PushNotificationRepository pushNotificationRepository = new PushNotificationDataRepository();
        DeleteSavedPushNotificationByCategoryUseCase deleteUseCase =
                new DeleteSavedPushNotificationByCategoryUseCase(
                        new JobExecutor(),
                        new UIThread(),
                        pushNotificationRepository
                );
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(DeleteSavedPushNotificationByCategoryUseCase.PARAM_CATEGORY, category);
        deleteUseCase.execute(requestParams, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    CommonUtils.dumper("Success Clear Storage Notification");
                }
                else {
                    CommonUtils.dumper("Failed Clear Storage Notification");
                }
            }
        });
    }

    public static void clearCacheIfFromNotification(String category, String serverId){
        PushNotificationRepository pushNotificationRepository = new PushNotificationDataRepository();
        DeleteSavedPushNotificationByCategoryAndServerIdUseCase deleteUseCase =
                new DeleteSavedPushNotificationByCategoryAndServerIdUseCase(
                        new JobExecutor(),
                        new UIThread(),
                        pushNotificationRepository
                );
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(DeleteSavedPushNotificationByCategoryAndServerIdUseCase.PARAM_CATEGORY, category);
        requestParams.putString(DeleteSavedPushNotificationByCategoryAndServerIdUseCase.PARAM_SERVER_ID, serverId);
        deleteUseCase.execute(requestParams, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    CommonUtils.dumper("Success Clear Storage Notification");
                }
                else {
                    CommonUtils.dumper("Failed Clear Storage Notification");
                }
            }
        });
    }

    public void cancelNotif() {
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    	notificationManager.cancel(100);
    	LocalCacheHandler.clearCache(mContext, TkpdCache.GCM_NOTIFICATION);
    }

}
