package com.tokopedia.shop_widget.common.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateHelper {
    private const val DEFAULT_SERVER_TIMEZONE = "Asia/Jakarta"
    private val DEFAULT_LOCALE = Locale("in", "ID")

    fun getDateFromString(dateString: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", DEFAULT_LOCALE)
        format.timeZone = TimeZone.getTimeZone(DEFAULT_SERVER_TIMEZONE)
        return try {
            format.parse(dateString)?: Date()
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }
}