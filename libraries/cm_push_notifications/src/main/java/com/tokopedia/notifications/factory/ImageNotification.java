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

    ImageNotification(Context context, BaseNotificationModel baseNotificationModel) {
        super(context, baseNotificationModel);
    }

    @Override
    public Notification createNotification() {
        NotificationCompat.Builder builder = getBuilder();
        builder.setContentTitle(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getTitle()));
        builder.setContentText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getMessage()));
        builder.setSmallIcon(getDrawableIcon());
        builder.setContentIntent(createMainPendingIntent(baseNotificationModel, getRequestCode()));
        builder.setAutoCancel(true);
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.getNotificationId(), getRequestCode()));
        setBigPictureNotification(builder, baseNotificationModel);
        return builder.build();
    }

    private void setBigPictureNotification(NotificationCompat.Builder builder, BaseNotificationModel baseNotificationModel) {
        Bitmap bitmap = CMNotificationUtils.loadBitmapFromUrl(baseNotificationModel.getMedia().getMediumQuality());
        if (null != bitmap) {
            builder.setLargeIcon(bitmap);
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                    .setSummaryText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getDetailMessage()))
                    .bigLargeIcon(getBlankBitmap())
                    .bigPicture(bitmap);
            if (!TextUtils.isEmpty(baseNotificationModel.getMessage()))
                bigPictureStyle.setSummaryText(CMNotificationUtils
                        .getSpannedTextFromStr(baseNotificationModel.getMessage()));
            builder.setStyle(bigPictureStyle);
        } else {
            //TODO use fallbackUrl
        }
    }

    private Bitmap getBlankBitmap() {
        int w = 72, h = 72;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        return Bitmap.createBitmap(w, h, conf);
    }

}
