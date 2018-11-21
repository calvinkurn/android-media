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
        builder.setContentText(baseNotificationModel.getMessage());
        builder.setSmallIcon(getDrawableIcon());
        builder.setContentIntent(createPendingIntent(baseNotificationModel.getAppLink(), 100));
        builder.setAutoCancel(true);
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.getNotificationId()));
        setBigPictureNotification(builder, baseNotificationModel);
        return builder.build();
    }

    private void setBigPictureNotification(NotificationCompat.Builder builder, BaseNotificationModel baseNotificationModel) {
        Bitmap bitmap = CMNotificationUtils.loadBitmapFromUrl(baseNotificationModel.getBigImageURL());
        if (null != bitmap) {
            builder.setLargeIcon(bitmap);
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                    .setSummaryText(baseNotificationModel.getDetailMessage())
                    .bigLargeIcon(getBlankBitmap())
                    .bigPicture(bitmap);
            if (!TextUtils.isEmpty(baseNotificationModel.getMessage()))
                bigPictureStyle.setSummaryText(baseNotificationModel.getMessage());
            builder.setStyle(bigPictureStyle);
        }
    }

    private Bitmap getBlankBitmap(){
        int w = 72, h = 72;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        return Bitmap.createBitmap(w, h, conf);
    }

}
