package com.tokopedia.notifications.database.convertors

import androidx.room.TypeConverter
import com.tokopedia.notifications.model.NotificationMode

class NotificationModeConverter {

    @TypeConverter
    fun toMode(modeInt: Int?): NotificationMode {
        return when (modeInt) {
            NotificationMode.POST_NOW.modeInt -> NotificationMode.POST_NOW
            else -> NotificationMode.OFFLINE
        }
    }

    @TypeConverter
    fun toInteger(mode: NotificationMode?): Int {
        return mode?.let { mode.modeInt} ?: return NotificationMode.OFFLINE.modeInt
    }

    companion object{
        val instances =  NotificationModeConverter()
    }

}