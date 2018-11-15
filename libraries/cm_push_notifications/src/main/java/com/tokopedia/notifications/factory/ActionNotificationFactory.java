package com.tokopedia.notifications.factory;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.tokopedia.notifications.model.ActionButton;
import com.tokopedia.notifications.model.BaseNotificationModel;

import java.util.List;

/**
 * @author lalit.singh
 */
public class ActionNotificationFactory extends BaseNotificationFactory {

    public ActionNotificationFactory(Context context) {
        super(context);
    }

    @Override
    public Notification createNotification(BaseNotificationModel baseNotificationModel, int notificationId) {
        NotificationCompat.Builder  builder = getBuilder();
        builder.setContentTitle(baseNotificationModel.getTitle());
        builder.setContentText(baseNotificationModel.getDesc());
        builder.setSmallIcon(getDrawableIcon());
        builder.setLargeIcon(getBitmapLargeIcon());
        builder.setContentIntent(createPendingIntent(baseNotificationModel.getApplink(), 100, notificationId));
        builder.setAutoCancel(true);
        builder.setDeleteIntent(createDismissPendingIntent(notificationId));
        addActionButton(baseNotificationModel.getActionButton(), builder, notificationId);

        return builder.build();
    }

    private void addActionButton(List<ActionButton> actionButtonList, NotificationCompat.Builder builder, int notificationId){
        for(ActionButton actionButton: actionButtonList){
            builder.addAction(tokopedia.applink.R.mipmap.ic_launcher,
                    actionButton.getText(), createPendingIntent(actionButton.getApplink(),100, notificationId));
        }
    }


}
