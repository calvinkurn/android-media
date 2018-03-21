package com.tokopedia.pushnotif;

import android.content.Context;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.tokopedia.pushnotif.db.model.HistoryNotificationDB;
import com.tokopedia.pushnotif.model.HistoryNotificationModel;
import com.tokopedia.pushnotif.db.model.HistoryNotificationDB_Table;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ricoharisin .
 */

public class HistoryNotification {

    private int NotificationType;

    public HistoryNotification(Context context, String key) {
        /*this.key = key;
        localCacheHandler = new LocalCacheHandler(context, NOTIFICATION_STORAGE);
        ArrayList<String> notificationString = localCacheHandler.getArrayListString(key);

        convertToHistoryNotificationModel(notificationString);*/

    }

    public void storeNotification(String message, String senderName) {
        HistoryNotificationDB historyNotificationDB = new HistoryNotificationDB();
        historyNotificationDB.setMessage(message);
        historyNotificationDB.setSenderName(senderName);
        historyNotificationDB.setNotificationType(NotificationType);
        historyNotificationDB.save();
    }

    public List<HistoryNotificationDB> getListHistoryNotification() {
        return SQLite.select().from(HistoryNotificationDB.class)
                .where(HistoryNotificationDB_Table.notification_type.eq(NotificationType))
                .queryList();

    }


    public static void clearHistoryNotification(Context context, String key) {
        /*LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, NOTIFICATION_STORAGE);
        localCacheHandler.clearCache(key);*/
    }



}
