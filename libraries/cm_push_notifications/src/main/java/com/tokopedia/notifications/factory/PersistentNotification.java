package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.tokopedia.notifications.R;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.model.BaseNotificationModel;
import com.tokopedia.notifications.model.PersistentButton;
import com.tokopedia.notifications.receiver.CMBroadcastReceiver;

import java.util.List;

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
        builder.setSmallIcon(getDrawableIcon());
        builder.setContentIntent(createPendingIntent(baseNotificationModel.getAppLink(), 100));
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

        List<PersistentButton> persistentButtonList = baseNotificationModel.getPersistentButtonList();
        int listSize = persistentButtonList.size();
        PersistentButton persistentButton;
        if (listSize > 0) {
            persistentButton = persistentButtonList.get(0);
            remoteView.setTextViewText(R.id.title1, persistentButton.getBtnText());
            remoteView.setImageViewBitmap(R.id.image_icon1, getBitmap(persistentButton.getBtnImageUrl()));
            remoteView.setOnClickPendingIntent(R.id.lin_container_1, createPendingIntent(persistentButton.getAppLink(), 100));
        }
        if (listSize > 1) {
            remoteView.setViewVisibility(R.id.lin_container_2, View.VISIBLE);
            persistentButton = persistentButtonList.get(1);
            remoteView.setTextViewText(R.id.title2, persistentButton.getBtnText());
            remoteView.setImageViewBitmap(R.id.image_icon2, getBitmap(persistentButton.getBtnImageUrl()));
            remoteView.setOnClickPendingIntent(R.id.lin_container_2, createPendingIntent(persistentButton.getAppLink(), 100));
        }
        if (listSize > 2) {
            remoteView.setViewVisibility(R.id.lin_container_3, View.VISIBLE);
            persistentButton = persistentButtonList.get(2);
            remoteView.setTextViewText(R.id.title3, persistentButton.getBtnText());
            remoteView.setImageViewBitmap(R.id.image_icon3, getBitmap(persistentButton.getBtnImageUrl()));
            remoteView.setOnClickPendingIntent(R.id.lin_container_3, createPendingIntent(persistentButton.getAppLink(), 100));
        }
        if (listSize > 3) {
            remoteView.setViewVisibility(R.id.lin_container_4, View.VISIBLE);
            persistentButton = persistentButtonList.get(3);
            remoteView.setTextViewText(R.id.title4, persistentButton.getBtnText());
            remoteView.setImageViewBitmap(R.id.image_icon4, getBitmap(persistentButton.getBtnImageUrl()));
            remoteView.setOnClickPendingIntent(R.id.lin_container_4, createPendingIntent(persistentButton.getAppLink(), 100));
        }

        return remoteView;
    }

    private PendingIntent getPersistentClosePIntent() {
        Intent intent = new Intent(context, CMBroadcastReceiver.class);
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.getNotificationId());
        intent.setAction(CMConstant.ReceiverAction.ACTION_CANCEL_PERSISTENT);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            return PendingIntent.getBroadcast(
                    context,
                    baseNotificationModel.getNotificationId(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
    }
}
