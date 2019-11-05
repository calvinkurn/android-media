package com.tokopedia.notifications.database.convertors

import androidx.room.TypeConverter
import com.tokopedia.notifications.model.NotificationStatus

class NotificationStatusConverter {

    @TypeConverter
    fun toStatus(status: Int?): NotificationStatus {
        return when (status) {
            NotificationStatus.PENDING.statusInt -> NotificationStatus.PENDING
            NotificationStatus.ACTIVE.statusInt -> NotificationStatus.ACTIVE
            NotificationStatus.DELETE.statusInt -> NotificationStatus.DELETE
            NotificationStatus.COMPLETED.statusInt -> NotificationStatus.COMPLETED
            else -> NotificationStatus.PENDING
        }
    }

    @TypeConverter
    fun toInteger(status: NotificationStatus?): Int {
        return status?.let { status.statusInt } ?: return 0
    }

    companion object{
        val instances =  NotificationStatusConverter()
    }
}