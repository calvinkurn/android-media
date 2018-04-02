package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

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
    public Notification createNotification(ApplinkNotificationModel applinkNotificationModel, int notifcationType) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL);
        builder.setContentTitle(context.getResources().getString(R.string.title_general_push_notification));
        builder.setContentText(applinkNotificationModel.getDesc());
        builder.setSmallIcon(getDrawableIcon());
        builder.setLargeIcon(getBitmapLargeIcon());
        builder.setGroup(generateGroupKey(applinkNotificationModel.getApplinks()));
        builder.setContentIntent(createPendingIntent(applinkNotificationModel.getApplinks(), Constant.NotificationId.GENERAL));

        return builder.build();
    }
}
