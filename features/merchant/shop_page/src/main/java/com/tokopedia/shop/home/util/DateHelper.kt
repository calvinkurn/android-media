package com.tokopedia.shop.home.util

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    private const val DEFAULT_SERVER_TIMEZONE = "Asia/Jakarta"

    fun getDateFromString(dateString: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        format.timeZone = TimeZone.getTimeZone(DEFAULT_SERVER_TIMEZONE)
        return try {
            format.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }
}
