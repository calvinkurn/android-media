package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.model.BaseNotificationModel;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public class GeneralNotificationFactory extends BaseNotificationFactory {

    public GeneralNotificationFactory(Context context) {
        super(context);
    }

    @Override
    public Notification createNotification(BaseNotificationModel baseNotificationModel, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CMConstant.GENERAL);
        builder.setContentTitle(baseNotificationModel.getTitle());
        builder.setContentText(baseNotificationModel.getDesc());
        builder.setSmallIcon(getDrawableIcon());
        builder.setLargeIcon(getBitmapLargeIcon());
        builder.setContentIntent(createPendingIntent(baseNotificationModel.getApplink(), 100, notificationId));
        builder.setDeleteIntent(createDismissPendingIntent(notificationId));
        builder.setAutoCancel(true);

        return builder.build();
    }
}
