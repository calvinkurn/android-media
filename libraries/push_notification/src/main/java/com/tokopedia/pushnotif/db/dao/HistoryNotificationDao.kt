package com.tokopedia.pushnotif.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tokopedia.pushnotif.db.model.HistoryNotificationDB

/**
 * @author okasurya on 5/22/19.
 */
@Dao
interface HistoryNotificationDao {
    @Insert
    fun storeNotification(data: HistoryNotificationDB): Long

    @Query("SELECT * FROM HistoryNotificationDB WHERE notification_type=:notificationType ORDER BY id DESC LIMIT :limit")
    fun getListHistoryNotification(notificationType: Int, limit: Int): List<HistoryNotificationDB>

    @Query("DELETE FROM HistoryNotificationDB WHERE notification_type=:notificationType AND notification_id=:notificationId")
    fun clearHistoryNotification(notificationType: Int, notificationId: Int)

    @Query("DELETE FROM HistoryNotificationDB WHERE notification_type=:notificationType")
    fun clearAllHistoryNotification(notificationType: Int)

    @Query("SELECT COUNT(id) FROM HistoryNotificationDB WHERE notification_type=:notificationType")
    fun countNotification(notificationType: Int): Int
}