package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.tokopedia.notifications.common.CMNotificationUtils;
import com.tokopedia.notifications.model.BaseNotificationModel;

/**
 * @author lalit.singh
 */
public class ImageNotification extends BaseNotification {


    public ImageNotification(Context context, BaseNotificationModel baseNotificationModel, int notificationId) {
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
        builder.setAutoCancel(true);
        builder.setDeleteIntent(createDismissPendingIntent(notificationId));
        setBigPictureNotification(builder, baseNotificationModel);
        return builder.build();
    }

    private void setBigPictureNotification(NotificationCompat.Builder builder, BaseNotificationModel baseNotificationModel) {
        Bitmap bitmap = CMNotificationUtils.loadBitmapFromUrl(baseNotificationModel.getBigImageURL());
        if (null != bitmap) {
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap);
            if (!TextUtils.isEmpty(baseNotificationModel.getMessage()))
                bigPictureStyle.setSummaryText(baseNotificationModel.getMessage());
            builder.setStyle(bigPictureStyle);
        }
    }

}
