package com.tokopedia.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.factory.BaseNotification;
import com.tokopedia.notifications.factory.CMNotificationFactory;

/**
 * @author lalit.singh
 */
public class CMJobIntentService extends JobIntentService {

    private static int JOB_ID = 131;

    static String TAG = CMJobIntentService.class.getSimpleName();

    public static void enqueueWork(Context context, Bundle bundle) {
        Intent work = new Intent();
        work.putExtra(CMConstant.EXTRA_NOTIFICATION_BUNDLE, bundle);
        enqueueWork(context, CMJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        try {
            Bundle bundle = intent.getBundleExtra(CMConstant.EXTRA_NOTIFICATION_BUNDLE);
            if (null != bundle) {
                if (bundle.getString(CMConstant.PayloadKeys.NOTIFICATION_TYPE, "")
                        .equals(CMConstant.NotificationType.SILENT_PUSH)) {
                    handleSilentPush(bundle);
                } else {
                    BaseNotification baseNotification = CMNotificationFactory.getNotification(this.getApplicationContext(), bundle);
                    if (null != baseNotification)
                        postNotification(baseNotification);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void postNotification(BaseNotification baseNotification) {
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = baseNotification.createNotification();
        if (null != notificationManager)
            notificationManager.notify(baseNotification.baseNotificationModel.getNotificationId(), notification);
    }

    private void handleSilentPush(Bundle data) {

    }

}
