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

    fun convertTime(date: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val now = ZonedDateTime.now()
            val convert = ZonedDateTime.parse(date)
            val diff = Duration.between(convert, now)
            val minute = diff.toMinutes()
            val hour = diff.toHours()
            val day = diff.toDays()

            return if (day in 1..90) {
                "$day $DAY"
            } else if (day > 90) {
                "${convert.month.name.take(3).capitalize()} ${convert.year}"
            } else if (hour in 1..24) {
                "$hour $HOUR"
            } else if (minute in 1..60) {
                "$minute $MINUTE"
            } else {
                DEFAULT_WORDING
            }
        } else {
            val convert = date.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS)
            val diff = DateUtil.getCurrentCalendar().time.time - convert.time
            val minute = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)
            val hour = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)
            val day = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            return if (day in 1..90) {
                "$day $DAY"
            } else if (day > 90) {
                "${convert.month} ${convert.year}"
            } else if (hour in 1..24) {
                "$hour $HOUR"
            } else if (minute in 1..60) {
                "$minute $MINUTE"
            } else {
                DEFAULT_WORDING
            }
        }
    }

}
