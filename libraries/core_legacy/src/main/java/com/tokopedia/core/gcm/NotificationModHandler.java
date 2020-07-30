package com.tokopedia.core.gcm;

import android.app.NotificationManager;
import android.content.Context;

import com.tokopedia.core.deprecated.LocalCacheHandler;
import com.tokopedia.core.gcm.data.PushNotificationDataRepository;
import com.tokopedia.core.gcm.domain.PushNotificationRepository;
import com.tokopedia.core.gcm.domain.usecase.DeleteSavedPushNotificationUseCase;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.usecase.RequestParams;

import rx.Subscriber;
import timber.log.Timber;

public class NotificationModHandler {

    private Context mContext;

    public NotificationModHandler(Context context) {
        this.mContext = context;
    }

    public static void clearCacheAllNotification(Context activity) {
        PushNotificationRepository pushNotificationRepository = new PushNotificationDataRepository(activity);
        DeleteSavedPushNotificationUseCase deleteUseCase =
                new DeleteSavedPushNotificationUseCase(
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
                    Timber.d("Success Clear Storage Notification");
                else
                    Timber.d("Failed Clear Storage Notification");
            }
        });
    }

    public void dismissAllActivedNotifications() {
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
