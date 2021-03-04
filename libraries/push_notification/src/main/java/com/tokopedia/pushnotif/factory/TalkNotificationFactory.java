package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.tokopedia.pushnotif.ApplinkNotificationHelper;
import com.tokopedia.pushnotif.data.constant.Constant;
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel;

/**
 * @author ricoharisin .
 */

public class TalkNotificationFactory extends BaseNotificationFactory {

    public TalkNotificationFactory(Context context) {
        super(context);
    }

    @Override
    public Notification createNotification(ApplinkNotificationModel applinkNotificationModel, int notificationType, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL);
        builder.setContentTitle(applinkNotificationModel.getDesc());
        builder.setContentText(applinkNotificationModel.getFullName()+" : "+applinkNotificationModel.getSummary());
        builder.setSmallIcon(getDrawableIcon());
        builder.setLargeIcon(getBitmap(applinkNotificationModel.getThumbnail()));
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(applinkNotificationModel.getFullName() + " : " + applinkNotificationModel.getSummary()));
        if (ApplinkNotificationHelper.allowGroup()) {
            builder.setGroup(generateGroupKey(applinkNotificationModel.getApplinks()));
        }
        builder.setContentIntent(createPendingIntent(applinkNotificationModel.getApplinks(), notificationType, notificationId));
        builder.setDeleteIntent(createDismissPendingIntent(notificationType, notificationId));
        builder.setAutoCancel(true);

        if (isAllowBell()) {
            builder.setSound(getRingtoneUri());
            if (isAllowVibrate()) builder.setVibrate(getVibratePattern());
        }

        return builder.build();
    }
}
