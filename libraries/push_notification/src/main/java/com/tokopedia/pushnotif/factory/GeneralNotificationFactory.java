package com.tokopedia.pushnotif.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;

import com.tokopedia.pushnotif.ApplinkNotificationHelper;
import com.tokopedia.pushnotif.data.constant.Constant;
import com.tokopedia.pushnotif.R;
import com.tokopedia.pushnotif.data.model.ApplinkNotificationModel;

/**
 * @author ricoharisin .d
 */

public class GeneralNotificationFactory extends BaseNotificationFactory {
    public GeneralNotificationFactory(Context context) {
        super(context);
    }

    @Override
    public Notification createNotification(ApplinkNotificationModel applinkNotificationModel, int notificationType, int notificationId) {
        storeToTransaction(context, notificationType, notificationId, applinkNotificationModel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.NotificationChannel.GENERAL);
        builder.setContentTitle(TextUtils.isEmpty(applinkNotificationModel.getTitle()) ? context.getResources().getString(R.string.title_general_push_notification) : applinkNotificationModel.getTitle());
        builder.setContentText(applinkNotificationModel.getDesc());
        builder.setSmallIcon(getDrawableIcon());
        builder.setLargeIcon(getBitmapLargeIcon());
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText(applinkNotificationModel.getDesc()));
        if (ApplinkNotificationHelper.allowGroup())
            builder.setGroup(generateGroupKey(applinkNotificationModel.getApplinks()));
        PendingIntent pendingContentIntent = createPendingIntent(applinkNotificationModel.getApplinks(), Constant.NotificationId.GENERAL, notificationId, applinkNotificationModel);
        builder.setContentIntent(pendingContentIntent);
        builder.setAutoCancel(true);

        if (isAllowBell()) {
            builder.setSound(getRingtoneUri());
            if (isAllowVibrate()) builder.setVibrate(getVibratePattern());
        }

        if (applinkNotificationModel.hasImages()) {
            setBigPictureNotification(builder, applinkNotificationModel);
        }

        return builder.build();
    }

    private void setBigPictureNotification(
            NotificationCompat.Builder builder,
            ApplinkNotificationModel applinkNotificationModel
    ) {
        String imageUrl = applinkNotificationModel.getBigPictureImageUrl();
        Bitmap image = getBitmap(imageUrl);

        NotificationCompat.Style bigPictureStyle = new NotificationCompat.BigPictureStyle()
                .bigPicture(image);

        builder.setStyle(bigPictureStyle);
    }
}
