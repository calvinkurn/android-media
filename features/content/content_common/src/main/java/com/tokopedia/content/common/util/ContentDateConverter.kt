package com.tokopedia.content.common.util

import java.time.Duration
import java.time.ZonedDateTime

/**
 * @author by astidhiyaa on 10/05/23
 */
object ContentDateConverter {
    private const val DAY = "hari"
    private const val HOUR = "jam"
    private const val MINUTE = "menit"
    private const val DEFAULT_WORDING = "Beberapa detik yang lalu"

    fun convertTime(date: String): String {
        return try {
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
        } catch (e: Exception) {
            DEFAULT_WORDING
        }
    }

}
