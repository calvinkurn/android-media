package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.tokopedia.pushnotif.ApplinkNotificationHelper;
import com.tokopedia.pushnotif.Constant;
import com.tokopedia.pushnotif.R;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;

/**
 * @author ricoharisin .d
 */

public class GeneralNotificationFactory extends BaseNotificationFactory {
    public GeneralNotificationFactory(Context context) {
        super(context);
    }

    @Override
    public Notification createNotification(ApplinkNotificationModel applinkNotificationModel, int notifcationType, int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL);
        builder.setContentTitle(TextUtils.isEmpty(applinkNotificationModel.getTitle()) ? context.getResources().getString(R.string.title_general_push_notification) : applinkNotificationModel.getTitle());
        builder.setContentText(applinkNotificationModel.getDesc());
        builder.setSmallIcon(getDrawableIcon());
        builder.setLargeIcon(getBitmapLargeIcon());
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(applinkNotificationModel.getDesc()));
        if (ApplinkNotificationHelper.allowGroup())
            builder.setGroup(generateGroupKey(applinkNotificationModel.getApplinks()));
        builder.setContentIntent(createPendingIntent(applinkNotificationModel.getApplinks(), Constant.NotificationId.GENERAL, notificationId));
        builder.setAutoCancel(true);

        if (isAllowBell()) {
            builder.setSound(getRingtoneUri());
            if (isAllowVibrate()) builder.setVibrate(getVibratePattern());
        }

        return builder.build();
    }
}
