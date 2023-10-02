package com.tokopedia.tokochat.common.util

import android.text.format.DateUtils
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TokoChatTimeUtil {

    const val DATE_FORMAT = "d MMM yyyy"
    const val RELATIVE_TODAY = "Hari ini"
    const val RELATIVE_YESTERDAY = "Kemarin"
    const val HEADER_DATE_FORMAT = "d MMMM, yyyy"

    private val DAYS_90_IN_SECOND = TimeUnit.DAYS.toMillis(90)
    private val DAYS_6_IN_SECOND = TimeUnit.DAYS.toMillis(6)
    private val DAYS_1_IN_SECOND = TimeUnit.DAYS.toMillis(1)
    private val HOUR_1_IN_SECOND = TimeUnit.HOURS.toMillis(1)
    private val MINUTE_1_IN_SECOND = TimeUnit.MINUTES.toMillis(1)

    fun getRelativeDate(
        date: String = "",
        dateTimestamp: Long
    ): String {
        return when {
            DateUtils.isToday(dateTimestamp) -> RELATIVE_TODAY
            DateUtils.isToday(dateTimestamp + DateUtils.DAY_IN_MILLIS) -> RELATIVE_YESTERDAY
            else -> {
                if (date.isNotBlank()) {
                    DateFormatUtils.formatDate(DATE_FORMAT, HEADER_DATE_FORMAT, date, Locale.ENGLISH)
                } else {
                    DateFormatUtils.getFormattedDate(dateTimestamp, DATE_FORMAT)
                }
            }
        }
    }

    fun getRelativeTime(timeMillis: Long, locale: Locale = getLocale()): String {
        val diff: Long = getNowTimeStamp() - timeMillis
        return when {
            diff / DAYS_90_IN_SECOND > 0 -> format(timeMillis, "MMM yyyy", locale)
            diff / DAYS_6_IN_SECOND > 0 -> format(timeMillis, "dd MMM", locale)
            diff / DAYS_1_IN_SECOND > 0 -> "${diff / DAYS_1_IN_SECOND} hari lalu"
            diff / HOUR_1_IN_SECOND > 0 -> "${diff / HOUR_1_IN_SECOND} jam"
            diff / MINUTE_1_IN_SECOND > 0 -> "${diff / MINUTE_1_IN_SECOND} mnt"
            else -> "<1 mnt"
        }
    }

    private fun getNowTimeStamp(): Long {
        val date = Calendar.getInstance(getLocale())
        return date.timeInMillis
    }

    private fun format(timeMillis: Long, pattern: String, locale: Locale): String {
        val sdf = SimpleDateFormat(pattern, locale)
        return sdf.format(timeMillis)
    }

    private fun getLocale(): Locale {
        return Locale.getDefault()
    }
}
