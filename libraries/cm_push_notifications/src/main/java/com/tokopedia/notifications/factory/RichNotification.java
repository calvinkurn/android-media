package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

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
public class RichNotification extends BaseNotification {

    RichNotification(Context context, BaseNotificationModel baseNotificationModel) {
        super(context, baseNotificationModel);
    }

    @Override
    public Notification createNotification() {
        NotificationCompat.Builder builder = getBuilder();
        if (null == builder)
            return null;
        builder.setContentTitle(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getTitle()));
        builder.setContentText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getMessage()));
        builder.setSmallIcon(getDrawableIcon());
        builder.setContentIntent(createMainPendingIntent(baseNotificationModel, getRequestCode()));
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.getNotificationId(), getRequestCode()));
        builder.setAutoCancel(true);
        setBigStyle(builder);
        addActionButton(baseNotificationModel.getActionButton(), builder);
        return builder.build();
    }

    private void addActionButton(List<ActionButton> actionButtonList, NotificationCompat.Builder builder) {
        for (ActionButton actionButton : actionButtonList) {
            builder.addAction(R.drawable.qc_launcher,
                    actionButton.getText(), getButtonPendingIntent(actionButton, getRequestCode()));
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

    private void setBigStyle(NotificationCompat.Builder builder) {
        Bitmap bitmap = CMNotificationUtils.loadBitmapFromUrl(baseNotificationModel.getMedia().getMediumQuality());
        if (null != bitmap) {
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.setBigContentTitle(baseNotificationModel.getTitle());
            if (baseNotificationModel.getDetailMessage() != null) {
                bigPictureStyle.setSummaryText(CMNotificationUtils.getSpannedTextFromStr(baseNotificationModel.getDetailMessage()));
            }
            bigPictureStyle.bigPicture(bitmap);
            bigPictureStyle.bigLargeIcon(getBlankBitmap());
            builder.setStyle(bigPictureStyle);
        }
    }

    private Bitmap getBlankBitmap() {
        int w = 72, h = 72;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        return Bitmap.createBitmap(w, h, conf);
    }

}


