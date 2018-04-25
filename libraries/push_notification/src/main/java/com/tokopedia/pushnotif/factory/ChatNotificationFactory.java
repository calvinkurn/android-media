package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.pushnotif.ApplinkNotificationHelper;
import com.tokopedia.pushnotif.Constant;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;

/**
 * @author ricoharisin .
 */

public class ChatNotificationFactory extends BaseNotificationFactory{
    public ChatNotificationFactory(Context context) {
        super(context);
    }

    @Override
    public Notification createNotification(ApplinkNotificationModel applinkNotificationModel, int notifcationType, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL);
        builder.setContentTitle(applinkNotificationModel.getDesc());
        builder.setContentText(applinkNotificationModel.getSummary());
        builder.setSmallIcon(getDrawableIcon());
        builder.setLargeIcon(getBitmap(applinkNotificationModel.getThumbnail()));
        builder.setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(applinkNotificationModel.getSummary()));
        if (ApplinkNotificationHelper.allowGroup()) builder.setGroup(generateGroupKey(applinkNotificationModel.getApplinks()));
        builder.setContentIntent(createPendingIntent(applinkNotificationModel.getApplinks(), notifcationType, notificationId));
        builder.setDeleteIntent(createDismissPendingIntent(notifcationType, notificationId));
        builder.setAutoCancel(true);

        if (isAllowBell()) {
            builder.setSound(getRingtoneUri());
            if (isAllowVibrate()) builder.setVibrate(getVibratePattern());
        }

        return builder.build();
    }
}
