package com.tokopedia.kelontongapp.notification;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;

import com.tokopedia.kelontongapp.KelontongConstant;

/**
 * Created by meta on 19/10/18.
 */
public class NotificationFactory {

    public static void show(Context context, Bundle data) {

        NotificationModel notificationModel = NotificationModel.convertBundleToModel(data);
        if (allowToShow(notificationModel)) {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            int notificationId = KelontongConstant.NotificationConstant.NOTIFICATION_ID_GENERAL;
            Notification notifChat = new NotificationBuilder(context, notificationModel, notificationId).build();

            notificationManagerCompat.notify(notificationId, notifChat);
        }
    }

    public static Boolean allowToShow(NotificationModel notificationModel) {
        return KelontongConstant.NotificationConstant.LOWER_CODE <= notificationModel.getTkpCode() && notificationModel.getTkpCode() <= KelontongConstant.NotificationConstant.UPPER_CODE;
    }
}
