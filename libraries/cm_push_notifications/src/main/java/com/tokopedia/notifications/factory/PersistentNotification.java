package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.tokopedia.notifications.R;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.model.BaseNotificationModel;
import com.tokopedia.notifications.model.PersistentNotificationData;
import com.tokopedia.notifications.receiver.DismissReceiver;
import com.tokopedia.notifications.receiver.PersistentCloseReceiver;

/**
 * @author lalit.singh
 */
public class PersistentNotification extends BaseNotification {


    public PersistentNotification(Context context, BaseNotificationModel baseNotificationModel, int notificationId) {
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
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        RemoteViews remoteViews = getPersistentRemoteView();
        builder.setCustomContentView(remoteViews);
        builder.setCustomBigContentView(remoteViews);
        return builder.build();
    }

    /*
    * create RemoteViews using BaseNotificationModel
    *
    * */
    private RemoteViews getPersistentRemoteView() {
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.persistent_notification_layout);
        remoteView.setOnClickPendingIntent(R.id.image_icon5, getPersistentClosePIntent());
        PersistentNotificationData data = baseNotificationModel.getPersistentNotificationData();

        remoteView.setTextViewText(R.id.title1, data.getBtnText());
        remoteView.setImageViewBitmap(R.id.image_icon1, getBitmap(data.getBtnImageUrl()));
        remoteView.setTextViewText(R.id.title2, data.getBtnText());
        remoteView.setImageViewBitmap(R.id.image_icon2, getBitmap(data.getBtnImageUrl()));
        remoteView.setTextViewText(R.id.title3, data.getBtnText());
        remoteView.setImageViewBitmap(R.id.image_icon3, getBitmap(data.getBtnImageUrl()));
        remoteView.setTextViewText(R.id.title4, data.getBtnText());
        remoteView.setImageViewBitmap(R.id.image_icon4, getBitmap(data.getBtnImageUrl()));

        return remoteView;
    }

    private PendingIntent getPersistentClosePIntent(){
        Intent intent = new Intent(context, PersistentCloseReceiver.class);
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, notificationId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            return PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
    }


    /*
    * task  - add PendingIntent to close Notification...
    *       - add PendingIntent to ActionButton on Persistent Notification
    *       -
    * */
}
