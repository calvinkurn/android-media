package com.tokopedia.shop_widget.common.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import java.util.concurrent.TimeUnit

object DateHelper {

    private const val DEFAULT_VIEW_FORMAT = "dd MMM yyyy"
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