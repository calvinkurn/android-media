package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.notifications.R;
import com.tokopedia.notifications.model.ActionButton;
import com.tokopedia.notifications.model.BaseNotificationModel;

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
        builder.setContentIntent(createPendingIntent(baseNotificationModel.getAppLink()));
        builder.setAutoCancel(true);
        builder.setDeleteIntent(createDismissPendingIntent(baseNotificationModel.getNotificationId()));
        addActionButton(baseNotificationModel.getActionButton(), builder);
        return builder.build();
    }

    private void addActionButton(List<ActionButton> actionButtonList, NotificationCompat.Builder builder) {
        if (!baseNotificationModel.getDetailMessage().isEmpty())
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(baseNotificationModel.getDetailMessage()));

        for (ActionButton actionButton : actionButtonList) {
            builder.addAction(R.drawable.qc_launcher,
                    actionButton.getText(), getButtonPendingIntent(actionButton));
        }
    }

}
