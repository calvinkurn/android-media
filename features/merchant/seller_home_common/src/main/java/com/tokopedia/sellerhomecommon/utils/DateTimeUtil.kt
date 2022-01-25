package com.tokopedia.sellerhomecommon.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 20/05/20
 */

object DateTimeUtil {

    const val FORMAT_DD_MMM_YYYY = "dd MMM yyyy"
    const val FORMAT_HH_MM = "HH:mm"
    const val ZERO = 0
    val ONE_DAY_MILLIS: Long = TimeUnit.DAYS.toMillis(1)

    fun getLocale(): Locale {
        return Locale("id")
    }

    fun format(timeMillis: Long, pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }

    fun format(
        timeMillis: Long,
        pattern: String,
        timeZone: TimeZone,
        locale: Locale = getLocale()
    ): String {
        val sdf = SimpleDateFormat(pattern, locale).apply {
            setTimeZone(timeZone)
        }
        return sdf.format(timeMillis)
    }

    fun getNPastDaysTimestamp(daysBefore: Long): Long {
        return Calendar.getInstance(getLocale()).timeInMillis.minus(
            TimeUnit.DAYS.toMillis(
                daysBefore
            )
        )
    }

    fun getNNextDaysTimestamp(days: Long): Long {
        return Calendar.getInstance(getLocale()).timeInMillis.plus(TimeUnit.DAYS.toMillis(days))
    }

    fun getFormattedDate(daysBefore: Long, format: String): String {
        return format(getNPastDaysTimestamp(daysBefore), format)
    }
}