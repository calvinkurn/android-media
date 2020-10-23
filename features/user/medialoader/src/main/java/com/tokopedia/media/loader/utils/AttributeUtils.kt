package com.tokopedia.media.loader.utils

import java.sql.Timestamp
import java.util.Date

object AttributeUtils {
    fun getDateTime(): String {
        val stamp = Timestamp(System.currentTimeMillis())
        val date = Date(stamp.time)
        return date.toString()
    }
}