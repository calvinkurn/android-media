package com.dompetia.sellerhomecommon.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 20/05/20
 */

object DateTimeUtil {

    fun getLocale(): Locale {
        return Locale("id")
    }

    fun format(timeMillis: Long, pattern: String, locale: Locale = getLocale()): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }

    fun getNPastDaysTimestamp(daysBefore: Long): Long {
        return Calendar.getInstance(getLocale()).timeInMillis.minus(TimeUnit.DAYS.toMillis(daysBefore))
    }

    fun getFormattedDate(daysBefore: Long, format: String) = format(getNPastDaysTimestamp(daysBefore), format)
}