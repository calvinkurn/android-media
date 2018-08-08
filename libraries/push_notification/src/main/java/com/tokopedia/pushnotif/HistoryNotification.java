package com.tokopedia.pushnotif;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.pushnotif.db.model.HistoryNotificationDB;
import com.tokopedia.pushnotif.model.HistoryNotificationModel;
import com.tokopedia.pushnotif.db.model.HistoryNotificationDB_Table;
import java.util.List;

/**
 * @author ricoharisin .
 */

public class HistoryNotification {

    private static final int HISTORY_NOTIFICATION_LIMIT = 5;

    public static void storeNotification(String senderName, String message, int notificationType, int notificationId) {
        HistoryNotificationDB historyNotificationDB = new HistoryNotificationDB();
        historyNotificationDB.setMessage(message);
        historyNotificationDB.setSenderName(senderName);
        historyNotificationDB.setNotificationType(notificationType);
        historyNotificationDB.setNotificationId(notificationId);
        historyNotificationDB.save();
    }

    public static List<HistoryNotificationDB> getListHistoryNotification(int notificationType) {
        return SQLite.select().from(HistoryNotificationDB.class)
                .where(HistoryNotificationDB_Table.notification_type.eq(notificationType))
                .orderBy(HistoryNotificationDB_Table.id, false)
                .limit(HISTORY_NOTIFICATION_LIMIT)
                .queryList();

    }


    public static void clearHistoryNotification(int notificationType, int notificationId) {
        SQLite.delete().from(HistoryNotificationDB.class)
                .where(HistoryNotificationDB_Table.notification_type.eq(notificationType))
                .and(HistoryNotificationDB_Table.notification_id.eq(notificationId))
                .execute();
    }

    public static void clearAllHistoryNotification(int notificationType) {
        SQLite.delete().from(HistoryNotificationDB.class)
                .where(HistoryNotificationDB_Table.notification_type.eq(notificationType))
                .execute();
    }

    public static Boolean isSingleNotification(int notificationType) {
        List<HistoryNotificationDB> listHistoryNotification = getListHistoryNotification(notificationType);
        return listHistoryNotification.size() == 0;
    }



}
