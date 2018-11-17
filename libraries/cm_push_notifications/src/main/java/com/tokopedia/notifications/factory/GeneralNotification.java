package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.model.BaseNotificationModel;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public class GeneralNotification extends BaseNotification {


    public GeneralNotification(Context context, BaseNotificationModel baseNotificationModel, int notificationId) {
        super(context, baseNotificationModel, notificationId);
    }

    @Override
    public Notification createNotification() {
        NotificationCompat.Builder builder = getBuilder();
        builder.setContentTitle(baseNotificationModel.getTitle());
        builder.setContentText(baseNotificationModel.getDesc());
        builder.setSmallIcon(getDrawableIcon());
        builder.setLargeIcon(getBitmapLargeIcon());
        builder.setContentIntent(createPendingIntent(baseNotificationModel.getApplink(), 100));
        builder.setDeleteIntent(createDismissPendingIntent(notificationId));
        builder.setAutoCancel(true);
        return builder.build();
    }
}
