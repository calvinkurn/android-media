package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.tokopedia.notifications.R;
import com.tokopedia.notifications.model.BaseNotificationModel;

/**
 * @author lalit.singh
 */
public class VisualNotification extends BaseNotification {


    public VisualNotification(Context context, BaseNotificationModel baseNotificationModel) {
        super(context, baseNotificationModel);
    }

    @Override
    public Notification createNotification() {
        NotificationCompat.Builder builder = getBuilder();
        builder.setSmallIcon(getDrawableIcon());
        builder.setContentIntent(createMainPendingIntent(baseNotificationModel, getRequestCode()));
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.getNotificationId(), getRequestCode()));
        RemoteViews remoteViews = new RemoteViews(context.getApplicationContext().getPackageName(),
                R.layout.layout_visual_collapsed);
        Bitmap bitmap = getBitmap(baseNotificationModel.getVisualCollapsedImageUrl());
        if (bitmap == null)
            return null;
        remoteViews.setImageViewBitmap(R.id.iv_collpasedImage, bitmap);
        builder.setCustomContentView(remoteViews);
        remoteViews = new RemoteViews(context.getApplicationContext().getPackageName(),
                R.layout.layout_visual_expand);
        bitmap = getBitmap(baseNotificationModel.getVisualExpandedImageUrl());
        if (bitmap == null)
            return null;
        remoteViews.setImageViewBitmap(R.id.iv_expanded, bitmap);
        builder.setCustomBigContentView(remoteViews);
        return builder.build();
    }
}
