package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.notifications.model.BaseNotificationModel;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public class GeneralNotification extends BaseNotification {


    public GeneralNotification(Context context, BaseNotificationModel baseNotificationModel) {
        super(context, baseNotificationModel);
    }

    @Override
    public Notification createNotification() {
        NotificationCompat.Builder builder = getBuilder();
        builder.setContentTitle(baseNotificationModel.getTitle());
        builder.setContentText(baseNotificationModel.getMessage());
        builder.setSmallIcon(getDrawableIcon());
        builder.setContentIntent(createPendingIntent(baseNotificationModel.getAppLink(), 100));
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.getNotificationId()));
        builder.setAutoCancel(true);
        if (!baseNotificationModel.getDetailMessage().isEmpty())
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(baseNotificationModel.getDetailMessage()));
        return builder.build();
    }
}
