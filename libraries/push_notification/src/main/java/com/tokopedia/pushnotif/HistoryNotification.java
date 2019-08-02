package com.tokopedia.pushnotif;

import android.content.Context;

import com.tokopedia.pushnotif.db.PushNotificationDB;
import com.tokopedia.pushnotif.db.model.HistoryNotificationDB;

import java.util.List;

/**
 * @author ricoharisin .
 */

public class HistoryNotification {

    private static final int HISTORY_NOTIFICATION_LIMIT = 5;

    public static void storeNotification(Context context, String senderName, String message, int notificationType, int notificationId) {
        HistoryNotificationDB data = new HistoryNotificationDB(
                senderName, message, notificationType, notificationId);
        PushNotificationDB.Companion.getInstance(context).historyNotificationDao()
                .storeNotification(data);
    }

    public static List<HistoryNotificationDB> getListHistoryNotification(Context context, int notificationType) {
        return PushNotificationDB.Companion.getInstance(context).historyNotificationDao()
                .getListHistoryNotification(notificationType, HISTORY_NOTIFICATION_LIMIT);
    }


    public static void clearHistoryNotification(Context context, int notificationType, int notificationId) {
        PushNotificationDB.Companion.getInstance(context).historyNotificationDao()
                .clearHistoryNotification(notificationType, notificationId);
    }

    public static void clearAllHistoryNotification(Context context, int notificationType) {
        PushNotificationDB.Companion.getInstance(context).historyNotificationDao()
                .clearAllHistoryNotification(notificationType);
    }

    public static Boolean isSingleNotification(Context context, int notificationType) {
        return PushNotificationDB.Companion.getInstance(context).historyNotificationDao()
                .countNotification(notificationType) == 0;
    }
}
