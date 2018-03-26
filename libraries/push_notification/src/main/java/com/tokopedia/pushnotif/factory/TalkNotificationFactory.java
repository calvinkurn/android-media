package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.pushnotif.Constant;
import com.tokopedia.pushnotif.model.ApplinkNotificationModel;

/**
 * @author ricoharisin .
 */

public class TalkNotificationFactory extends BaseNotificationFactory {

    public TalkNotificationFactory(Context context) {
        super(context);
    }

    public Notification createTalkNotification(ApplinkNotificationModel applinkNotificationModel, int notifcationType) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL);
        builder.setContentTitle(applinkNotificationModel.getDesc());
        builder.setContentText(applinkNotificationModel.getFullName()+" : "+applinkNotificationModel.getSummary());
        builder.setSmallIcon(getDrawableIcon());
        builder.setLargeIcon(getBitmap(applinkNotificationModel.getThumbnail()));
        builder.setGroup(generateGroupKey(applinkNotificationModel.getApplinks()));
        builder.setContentIntent(createPendingIntent(applinkNotificationModel.getApplinks(), Constant.NotificationId.GENERAL));

        return builder.build();
    }
}
