package com.tokopedia.sellerhome.util

import java.util.*
import java.util.concurrent.TimeUnit

object DateUtil {
    fun getNPastDaysTimestamp(daysBefore: Long) =
            Calendar.getInstance(Locale("id")).timeInMillis.minus(TimeUnit.DAYS.toMillis(daysBefore))

    fun getFormattedDate(daysBefore: Long, format: String) =
            TimeFormat.format(getNPastDaysTimestamp(daysBefore), format)
}