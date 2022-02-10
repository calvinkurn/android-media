package com.tokopedia.shop_widget.common.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

object DateHelper {
    private val DEFAULT_LOCALE = Locale("in", "ID")

    fun getDateFromString(dateString: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", DEFAULT_LOCALE)
        return try {
            format.parse(dateString)?: Date()
        } catch (e: Exception) {
            e.printStackTrace()
            Date()
        }
    }
}