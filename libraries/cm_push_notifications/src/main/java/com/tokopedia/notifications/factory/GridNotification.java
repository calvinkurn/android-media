package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.tokopedia.notifications.R;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.CMNotificationUtils;
import com.tokopedia.notifications.model.BaseNotificationModel;
import com.tokopedia.notifications.model.Grid;
import com.tokopedia.notifications.receiver.CMBroadcastReceiver;

import java.util.List;

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
        setGridData(expandedView);
        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView);


        return builder.build();
    }

    private void setGridData(RemoteViews remoteViews) {
        setCollapseData(remoteViews, baseNotificationModel);

        int count = baseNotificationModel.getGridList().size();
        List<Grid> gridList = baseNotificationModel.getGridList();
        Bitmap bitmap;
        if (count >= 1) {
            bitmap = getBitmap(gridList.get(0).getMedia().getMediumQuality());
            if (bitmap != null) {
                remoteViews.setImageViewBitmap(R.id.iv_gridOne, bitmap);
            }
            remoteViews.setOnClickPendingIntent(R.id.iv_gridOne,
                    getPendingIntent(getRequestCode(), gridList.get(0).getAppLink()));
        }

        if (count >= 2) {
            bitmap = getBitmap(gridList.get(1).getMedia().getMediumQuality());
            if (bitmap != null) {
                remoteViews.setImageViewBitmap(R.id.iv_gridTwo, bitmap);
            }
            remoteViews.setOnClickPendingIntent(R.id.iv_gridTwo,
                    getPendingIntent(getRequestCode(), gridList.get(1).getAppLink()));
        }
        if (count >= 4) {
            bitmap = getBitmap(gridList.get(2).getMedia().getMediumQuality());
            if (bitmap != null) {
                remoteViews.setImageViewBitmap(R.id.iv_gridThree, bitmap);
            }
            remoteViews.setOnClickPendingIntent(R.id.iv_gridThree,
                    getPendingIntent(getRequestCode(), gridList.get(2).getAppLink()));

            bitmap = getBitmap(gridList.get(3).getMedia().getMediumQuality());
            if (bitmap != null) {
                remoteViews.setImageViewBitmap(R.id.iv_gridFour, bitmap);
            }
            remoteViews.setOnClickPendingIntent(R.id.iv_gridFour,
                    getPendingIntent(getRequestCode(), gridList.get(3).getAppLink()));
        } else {
            remoteViews.setViewVisibility(R.id.ll_bottomGridParent, View.GONE);
        }

        if (count == 6) {
            bitmap = getBitmap(gridList.get(4).getMedia().getMediumQuality());
            if (bitmap != null) {
                remoteViews.setImageViewBitmap(R.id.iv_gridFive, bitmap);
            }
            remoteViews.setOnClickPendingIntent(R.id.iv_gridFive,
                    getPendingIntent(getRequestCode(), gridList.get(4).getAppLink()));

            bitmap = getBitmap(gridList.get(5).getMedia().getMediumQuality());
            if (bitmap != null) {
                remoteViews.setImageViewBitmap(R.id.iv_gridSix, bitmap);
            }
            remoteViews.setOnClickPendingIntent(R.id.iv_gridSix,
                    getPendingIntent(getRequestCode(), gridList.get(5).getAppLink()));
        } else {
            remoteViews.setViewVisibility(R.id.iv_gridFive, View.GONE);
            remoteViews.setViewVisibility(R.id.iv_gridSix, View.GONE);
        }
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
        remoteView.setTextViewText(R.id.tv_collapse_title, CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getTitle()));
        remoteView.setTextViewText(R.id.tv_collapsed_message, CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getMessage()));
        remoteView.setOnClickPendingIntent(R.id.collapseMainView, getPendingIntent(getRequestCode(),
                baseNotificationModel.getAppLink()));
    }


    private PendingIntent getPendingIntent(int requestCode, String appLink) {
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


//2/3:1/2

//