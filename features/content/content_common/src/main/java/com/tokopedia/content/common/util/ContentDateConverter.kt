package com.tokopedia.content.common.util

import android.os.Build
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import java.time.Duration
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author by astidhiyaa on 10/05/23
 */
object ContentDateConverter {

    const val DAY = "hari"
    const val HOUR = "jam"
    const val MINUTE = "menit"
    const val MINUTE_CONCISE = "mnt"
    const val BELOW_1_MINUTE_CONCISE = "<1 $MINUTE_CONCISE"
    private const val DEFAULT_WORDING = "Beberapa detik yang lalu"

    data class DateTime(
        val minute: Long,
        val hour: Long,
        val day: Long,
        val yearMonth: String
    )

    fun getDiffTime(
        date: String,
        mapToLiteral: ((DateTime) -> String)? = null,
    ): String {
        val dateTime: DateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val now = ZonedDateTime.now()
            val convert = ZonedDateTime.parse(date)
            val diff = Duration.between(convert, now)
            DateTime(minute = diff.toMinutes(), hour = diff.toHours(), day = diff.toDays(), yearMonth = "${convert.month.name.take(3).lowercase().replaceFirstChar { it.uppercaseChar() }} ${convert.year}")
        } else {
            TimeZone.setDefault(DateUtil.DEFAULT_TIMEZONE)
            val convert = date.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS)
            val diff = DateUtil.getCurrentCalendar().time.time - convert.time
            val minute = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)
            val hour = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)
            val day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)

            val yearMonth = DateUtil.formatDate(currentFormat = DateUtil.YYYY_MM_DD_T_HH_MM_SS, newFormat = "MMM yyyy", date)

            DateTime(minute = minute, hour = hour, day = day, yearMonth = yearMonth)
        }

        return mapToLiteral?.invoke(dateTime) ?: when {
            dateTime.day in 1..90 -> "${dateTime.day} $DAY"
            dateTime.day > 90 -> dateTime.yearMonth
            dateTime.hour in 1..24 -> "${dateTime.hour} $HOUR"
            dateTime.minute in 1..60 -> "${dateTime.minute} $MINUTE"
            else -> DEFAULT_WORDING
        }
    }
}
