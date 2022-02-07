package com.tokopedia.sellerhomecommon.utils

import com.tokopedia.kotlin.extensions.view.orZero
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 20/05/20
 */

object DateTimeUtil {

    const val FORMAT_DD_MM_YYYY = "dd-MM-yyyy"
    private const val DEFAULT_TIME_MILLIS = 0L

    fun getLocale(): Locale {
        return Locale("id")
    }

    fun format(timeMillis: Long, pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }

    fun format(timeMillis: Long, pattern: String, timeZone: TimeZone, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale).apply {
            setTimeZone(timeZone)
        }
        return sdf.format(timeMillis)
    }

    fun getNPastDaysTimestamp(daysBefore: Long): Long {
        return Calendar.getInstance(getLocale()).timeInMillis.minus(TimeUnit.DAYS.toMillis(daysBefore))
    }

    fun getNNextDaysTimestamp(days: Long): Long {
        return Calendar.getInstance(getLocale()).timeInMillis.plus(TimeUnit.DAYS.toMillis(days))
    }

    fun getFormattedDate(daysBefore: Long, format: String) = format(getNPastDaysTimestamp(daysBefore), format)

    fun getTimeInMillis(dateStr: String, format: String): Long {
        val sdf = SimpleDateFormat(format, getLocale())
        return try {
            val date = sdf.parse(dateStr)
            date?.time.orZero()
        } catch (e: RuntimeException) {
            DEFAULT_TIME_MILLIS
        }
    }
}