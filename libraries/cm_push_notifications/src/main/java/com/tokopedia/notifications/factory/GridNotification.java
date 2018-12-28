package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.tokopedia.notifications.R;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.model.BaseNotificationModel;
import com.tokopedia.notifications.model.PersistentButton;
import com.tokopedia.notifications.receiver.CMBroadcastReceiver;

import static android.app.Notification.BADGE_ICON_SMALL;

/**
 * @author lalit.singh
 */
public class GridNotification extends BaseNotification {

    GridNotification(Context context, BaseNotificationModel baseNotificationModel) {
        super(context, baseNotificationModel);
    }

    @Override
    public Notification createNotification() {
        NotificationCompat.Builder builder = getNotificationBuilder();
        RemoteViews collapsedView = new RemoteViews(context.getApplicationContext().getPackageName()
                , R.layout.layout_collapsed);
        setCollapseData(collapsedView, baseNotificationModel);
        RemoteViews expandedView = new RemoteViews(context.getApplicationContext().getPackageName(),
                R.layout.layout_grid_expand);
        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView);
        return builder.build();
    }

    private void setCollapseData(RemoteViews remoteView, BaseNotificationModel baseNotificationModel) {
        if (TextUtils.isEmpty(baseNotificationModel.getIcon())) {
            remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, getBitmapLargeIcon());
        } else {
            Bitmap iconBitmap = getBitmap(baseNotificationModel.getIcon());
            if (null != iconBitmap) {
                remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, iconBitmap);
            } else {
                remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, getBitmapLargeIcon());
            }
        }
        remoteView.setTextViewText(R.id.tv_collapse_title, baseNotificationModel.getTitle());
        remoteView.setTextViewText(R.id.tv_collapsed_message, baseNotificationModel.getMessage());
        remoteView.setOnClickPendingIntent(R.id.collapseMainView, getPersistentClickPendingIntent(getRequestCode(),
                baseNotificationModel.getAppLink()));
    }


    private PendingIntent getPersistentClickPendingIntent(int requestCode, String appLink) {
        PendingIntent resultPendingIntent;
        Intent intent = new Intent(context, CMBroadcastReceiver.class);
        intent.setAction(CMConstant.ReceiverAction.ACTION_GRID_CLICK);
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.getNotificationId());
        intent.putExtra(CMConstant.ReceiverExtraData.ACTION_APP_LINK, appLink);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            resultPendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        } else {
            resultPendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }
        return resultPendingIntent;
    }

}

/* int count = baseNotificationModel.getGridList().size();
        if (count >= 1) {

        }

        if (count >= 2) {

        }

        if (count >= 3) {

        }

        if (count >= 4) {

        }*/