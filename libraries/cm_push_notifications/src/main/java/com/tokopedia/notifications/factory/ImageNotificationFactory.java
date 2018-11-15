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
public class ImageNotificationFactory extends BaseNotificationFactory {

    static String TAG = ImageNotificationFactory.class.getSimpleName();

    private NotificationCompat.Builder builder;

    public ImageNotificationFactory(Context context) {
        super(context);
    }

    @Override
    public Notification createNotification(BaseNotificationModel baseNotificationModel, int notificationId) {
        builder = getBuilder();
        builder.setContentTitle(baseNotificationModel.getTitle());
        builder.setContentText(baseNotificationModel.getDesc());
        builder.setSmallIcon(getDrawableIcon());
        builder.setLargeIcon(getBitmapLargeIcon());
        builder.setContentIntent(createPendingIntent(baseNotificationModel.getApplink(), 100, notificationId));
        builder.setAutoCancel(true);
        builder.setDeleteIntent(createDismissPendingIntent(notificationId));
        setBigPictureNotification(baseNotificationModel);
        return builder.build();
    }

    private void setBigPictureNotification(BaseNotificationModel baseNotificationModel) {
        //todo Image link from BaseNotification Model
        Bitmap bitmap  = CMNotificationUtils.loadBitmapFromUrl("");
        if (null != bitmap) {
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap);
            if (!TextUtils.isEmpty(baseNotificationModel.getMessage()))
                bigPictureStyle.setSummaryText(baseNotificationModel.getMessage());
            builder.setStyle(bigPictureStyle);
        }
    }

}
