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
import com.tokopedia.notifications.model.ActionButton;
import com.tokopedia.notifications.model.BaseNotificationModel;
import com.tokopedia.notifications.receiver.CMBroadcastReceiver;

import java.util.List;

/**
 * @author lalit.singh
 */
public class ActionNotification extends BaseNotification {

    ActionNotification(Context context, BaseNotificationModel baseNotificationModel) {
        super(context, baseNotificationModel);
    }

    @Override
    public Notification createNotification() {
        NotificationCompat.Builder builder = getNotificationBuilder();
        RemoteViews collapsedView = new RemoteViews(context.getApplicationContext().getPackageName()
                , R.layout.layout_collapsed);
        setCollapseData(collapsedView, baseNotificationModel, true);

        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.getNotificationId(), getRequestCode()));
        RemoteViews expandedView = new RemoteViews(context.getApplicationContext().getPackageName(),
                R.layout.layout_big_image);
        builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView);
        if (baseNotificationModel.getMedia() != null &&
                !TextUtils.isEmpty((baseNotificationModel.getMedia().getMediumQuality()))) {
            expandedView.setImageViewBitmap(R.id.img_big,
                    CMNotificationUtils.loadBitmapFromUrl(baseNotificationModel.getMedia().getMediumQuality()));
        }
        if (CMNotificationUtils.hasActionButton(baseNotificationModel)) {
            addActionButton(baseNotificationModel.getActionButton(), expandedView);
        }
        setCollapseData(expandedView, baseNotificationModel, false);
        return builder.build();
    }

    private void setCollapseData(RemoteViews remoteView, BaseNotificationModel baseNotificationModel, boolean isCollapsed) {

        if (isCollapsed) {
            if (TextUtils.isEmpty(baseNotificationModel.getIcon())) {
                Bitmap iconBitmap = getBitmap(baseNotificationModel.getIcon());
                if (null != iconBitmap) {
                    remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, iconBitmap);
                } else {
                    remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, getBitmapLargeIcon());
                }
            } else if (baseNotificationModel.getMedia() != null) {
                Bitmap iconBitmap = getBitmap(baseNotificationModel.getMedia().getMediumQuality());
                if (null != iconBitmap) {
                    remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, iconBitmap);
                } else {
                    remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, getBitmapLargeIcon());
                }
            } else {
                remoteView.setImageViewBitmap(R.id.iv_icon_collapsed, getBitmapLargeIcon());
            }
        }

        remoteView.setTextViewText(R.id.tv_collapse_title, CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getTitle()));
        remoteView.setTextViewText(R.id.tv_collapsed_message, CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getMessage()));
        remoteView.setOnClickPendingIntent(R.id.collapseMainView, createMainPendingIntent(baseNotificationModel,
                getRequestCode()));
    }

    private void addActionButton(List<ActionButton> actionButtonList, RemoteViews expandedView) {
        ActionButton actionButton;
        expandedView.setViewVisibility(R.id.ll_action, View.VISIBLE);
        for (int i = 0; i < actionButtonList.size(); i++) {
            actionButton = actionButtonList.get(i);
            switch (i) {
                case 0:
                    expandedView.setViewVisibility(R.id.tv_button1, View.VISIBLE);
                    expandedView.setTextViewText(R.id.tv_button1, actionButton.getText());
                    expandedView.setOnClickPendingIntent(R.id.tv_button1, getButtonPendingIntent(actionButton, getRequestCode()));
                    break;
                case 1:
                    expandedView.setViewVisibility(R.id.tv_button2, View.VISIBLE);
                    expandedView.setTextViewText(R.id.tv_button2, actionButton.getText());
                    expandedView.setOnClickPendingIntent(R.id.tv_button2, getButtonPendingIntent(actionButton, getRequestCode()));
                    break;
                case 2:
                    expandedView.setViewVisibility(R.id.tv_button3, View.VISIBLE);
                    expandedView.setTextViewText(R.id.tv_button3, actionButton.getText());
                    expandedView.setOnClickPendingIntent(R.id.tv_button3, getButtonPendingIntent(actionButton, getRequestCode()));
                    break;
            }
        }

    }

    private PendingIntent getButtonPendingIntent(ActionButton actionButton, int requestCode) {
        PendingIntent resultPendingIntent;
        Intent intent = new Intent(context, CMBroadcastReceiver.class);
        intent.setAction(CMConstant.ReceiverAction.ACTION_BUTTON);
        intent.putExtra(CMConstant.EXTRA_NOTIFICATION_ID, baseNotificationModel.getNotificationId());
        intent.putExtra(CMConstant.ReceiverExtraData.ACTION_BUTTON_APP_LINK, actionButton.getAppLink());
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
