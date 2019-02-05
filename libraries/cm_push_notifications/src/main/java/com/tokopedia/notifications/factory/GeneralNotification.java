package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.notifications.common.CMNotificationUtils;
import com.tokopedia.notifications.model.BaseNotificationModel;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public class GeneralNotification extends BaseNotification {


    GeneralNotification(Context context, BaseNotificationModel baseNotificationModel) {
        super(context, baseNotificationModel);
    }

    @Override
    public Notification createNotification() {
        NotificationCompat.Builder builder = getBuilder();
        builder.setContentTitle(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getTitle()));
        builder.setContentText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getMessage()));
        builder.setSmallIcon(getDrawableIcon());
        builder.setContentIntent(createMainPendingIntent(baseNotificationModel, getRequestCode()));
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.getNotificationId(), getRequestCode()));
        builder.setAutoCancel(true);
        if (!baseNotificationModel.getDetailMessage().isEmpty())
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getDetailMessage())));
        return builder.build();
    }


}
