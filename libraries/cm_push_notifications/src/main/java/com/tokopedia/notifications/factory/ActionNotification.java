package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.notifications.R;
import com.tokopedia.notifications.common.CMConstant;
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
        NotificationCompat.Builder builder = getBuilder();
        builder.setContentTitle(baseNotificationModel.getTitle());
        builder.setContentText(baseNotificationModel.getMessage());
        builder.setSmallIcon(getDrawableIcon());
        builder.setContentIntent(createMainPendingIntent(baseNotificationModel.getAppLink(), getRequestCode()));
        builder.setAutoCancel(true);
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.getNotificationId(), getRequestCode()));
        if (baseNotificationModel.getActionButton() == null || baseNotificationModel.getActionButton().size() == 0)
            return null;
        addActionButton(baseNotificationModel.getActionButton(), builder);
        return builder.build();
    }

    private void addActionButton(List<ActionButton> actionButtonList, NotificationCompat.Builder builder) {
        if (!baseNotificationModel.getDetailMessage().isEmpty())
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(baseNotificationModel.getDetailMessage()));

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

}
