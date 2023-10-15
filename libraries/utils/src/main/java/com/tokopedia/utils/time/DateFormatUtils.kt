package com.tokopedia.utils.time

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateFormatUtils {
    private val TAG = DateFormatUtils::class.java.simpleName
    const val FORMAT_DD_MM_YYYY = "dd/MM/yyyy"
    const val FORMAT_DD_MMMM_YYYY = "dd MMMM yyyy"
    const val FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    const val FORMAT_D_MMMM_YYYY = "d MMMM yyyy"
    const val FORMAT_YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss"

    val DEFAULT_LOCALE: Locale = Locale("in", "ID")

    const val INVALID_TIME_IN_MILLIS = -1L

    fun formatDate(currentFormat: String, newFormat: String, dateString: String, locale: Locale = DEFAULT_LOCALE): String {
        return try {
            val fromFormat: DateFormat = SimpleDateFormat(currentFormat, locale)
            fromFormat.isLenient = false
            val toFormat: DateFormat = SimpleDateFormat(newFormat, locale)
            toFormat.isLenient = false
            val date: Date? = fromFormat.parse(dateString)
            if (date != null)
                toFormat.format(date)
            else dateString
        } catch (e: Exception) {
            e.printStackTrace()
            dateString
        }
    }

    fun getTimeInMilliseconds(format: String, dateString: String, locale: Locale = DEFAULT_LOCALE): Long {
        return try {
            val fromFormat: DateFormat = SimpleDateFormat(format, locale)
            val date = fromFormat.parse(dateString)
            date?.time ?: INVALID_TIME_IN_MILLIS
        } catch (e: Exception) {
            INVALID_TIME_IN_MILLIS
        }
    }

    private fun getFormattedDate(timeInMillis: Long, format: String?): String {
        val instance: Calendar = Calendar.getInstance()
        instance.setTimeInMillis(timeInMillis)
        val dateFormat: DateFormat = SimpleDateFormat(format, DEFAULT_LOCALE)
        return dateFormat.format(instance.getTime())
    }

    private fun getFormattedDateSeconds(timeInSeconds: Long, format: String?): String {
        return getFormattedDate(timeInSeconds * 1000L, format)
    }

    fun getFormattedDate(unixTimeStringInSeconds: String, format: String?): String {
        var timeInSeconds = 0L
        try {
            timeInSeconds = unixTimeStringInSeconds.toLong()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return getFormattedDateSeconds(timeInSeconds, format)
    }
}