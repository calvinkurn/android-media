package com.tokopedia.content.common.util

import android.os.Build
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

/**
 * @author by astidhiyaa on 10/05/23
 */
object ContentDateConverter {
    private const val DAY = "hari"
    private const val HOUR = "jam"
    private const val MINUTE = "menit"
    private const val DEFAULT_WORDING = "Beberapa detik yang lalu"

    data class Converted(
        val minute: Long,
        val hour: Long,
        val day: Long,
        val yearMonth: String
    )

    fun convertTime(date: String): String {
        val data: Converted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val now = ZonedDateTime.now()
            val convert = ZonedDateTime.parse(date)
            val diff = Duration.between(convert, now)
            Converted(minute = diff.toMinutes(), hour = diff.toHours(), day = diff.toDays(), yearMonth = "${convert.month.name.take(3).capitalize()} ${convert.year}")
        } else {
            val convert = date.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS)
            val diff = DateUtil.getCurrentCalendar().time.time - convert.time
            val minute = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)
            val hour = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)
            val day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            Converted(minute = minute, hour = hour, day = day, yearMonth = "${convert.month} ${convert.year}")
        }
        return if (data.day in 1..90) {
            "${data.day} $DAY"
        } else if (data.day > 90) {
            data.yearMonth
        } else if (data.hour in 1..24) {
            "${data.hour} $HOUR"
        } else if (data.minute in 1..60) {
            "${data.minute} $MINUTE"
        } else {
            DEFAULT_WORDING
        }
    }
}
