package com.tokopedia.utils.time

import com.tokopedia.utils.time.RfcDateTimeParser.RFC_3339
import java.text.SimpleDateFormat
import java.util.*

object TimeHelper {

    @JvmStatic
    fun timeBetweenRFC3339(startTime: String, endTime: String): Long {
        val start = RfcDateTimeParser.parseDateString(startTime, RFC_3339)?.time ?: 0
        val end = RfcDateTimeParser.parseDateString(endTime, RFC_3339)?.time ?: 0

        val diff = end - start
        return if (diff > 0) {
            diff
        } else {
            0
        }
    }

    @JvmStatic
    fun timeSinceNow(endTime: String): Long {
        val start = Calendar.getInstance().time.time
        val end = RfcDateTimeParser.parseDateString(endTime, RFC_3339)?.time ?: 0

        return end - start
    }

    @JvmStatic
    fun formatDate(date: Date, format: String): String {
        val sdf = SimpleDateFormat(format, getLocale())
        return sdf.format(date)
    }

    @JvmStatic
    fun getNowTimeStamp(): Long {
        val date = Calendar.getInstance(getLocale())
        return date.timeInMillis
    }

    @JvmStatic
    fun getNPastMonthTimeText(monthBefore: Int): String {
        val pastTwoYear = getNPastMonthTimeStamp(monthBefore)
        val pattern = "dd/MM/yyyy"
        return format(pastTwoYear, pattern)
    }

    @JvmStatic
    fun format(timeMillis: Long, pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }

    @JvmStatic
    fun getNPastMonthTimeStamp(monthBefore: Int): Long {
        val date = Calendar.getInstance(getLocale())
        date.set(Calendar.MONTH, date.get(Calendar.MONTH) - monthBefore)
        return date.timeInMillis
    }

    @JvmStatic
    fun getLocale(): Locale {
        return Locale.getDefault()
    }
}