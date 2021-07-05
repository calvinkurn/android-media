package com.tokopedia.notifications.database.pushRuleEngine

import androidx.room.*
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.model.NotificationStatus

@Dao
abstract class BaseNotificationDao {

    @Query("SELECT * FROM BaseNotificationModel WHERE notificationStatus = 0 AND startTime<= :currentTimeMillis AND endTime > :currentTimeMillis")
    abstract fun getPendingNotification(currentTimeMillis: Long): List<BaseNotificationModel>

    @Query("SELECT * FROM BaseNotificationModel WHERE notificationStatus = :status")
    abstract fun getNotificationByStatusList(status: NotificationStatus): List<BaseNotificationModel>

    @Query("Update BaseNotificationModel set notificationStatus = :status WHERE notificationId = :notificationId")
    abstract fun updateNotificationStatus(notificationId: Int, status: NotificationStatus)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertNotificationModel(notificationModel: BaseNotificationModel)

    @Query("DELETE FROM BaseNotificationModel WHERE endTime <= :olderThanExpiryMillis AND notificationStatus = :status")
    abstract fun deleteNotification(olderThanExpiryMillis: Long, status: NotificationStatus)

    @Query("SELECT * FROM BaseNotificationModel WHERE notificationId= :notificationId")
    abstract fun getNotificationById(notificationId: Int): BaseNotificationModel?

    @Query("SELECT * FROM BaseNotificationModel")
    abstract fun getNotification(): List<BaseNotificationModel>

}
