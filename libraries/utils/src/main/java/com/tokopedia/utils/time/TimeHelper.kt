package com.tokopedia.utils.time

import com.tokopedia.utils.time.RfcDateTimeParser.RFC_3339
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TimeHelper {

    private val DAYS_90_IN_SECOND = TimeUnit.DAYS.toMillis(90)
    private val DAYS_6_IN_SECOND = TimeUnit.DAYS.toMillis(6)
    private val DAYS_1_IN_SECOND = TimeUnit.DAYS.toMillis(1)
    private val HOUR_1_IN_SECOND = TimeUnit.HOURS.toMillis(1)
    private val MINUTE_1_IN_SECOND = TimeUnit.MINUTES.toMillis(1)
    val localeID = Locale("in", "ID")

    /**
     * @param timeMillis timestamp in milliseconds
     * @return True if param [timeMillis] is in the past
     */
    fun isBeforeCurrentTime(timeMillis: Long): Boolean {
        return timeMillis < getNowTimeStamp()
    }

    /**
     * @param timeMillis timestamp in milliseconds
     * @return True if param [timeMillis] is in the future
     */
    fun isAfterCurrentTime(timeMillis: Long): Boolean {
        return timeMillis >= getNowTimeStamp()
    }

    /**
     * @param timeMillis timestamp in milliseconds
     * @return True if param [timeMillis] is in the future within 24 hour
     */
    fun isIn24HourAfterCurrentTime(timeMillis: Long): Boolean {
        val currentTimestamp = getNowTimeStamp()
        val next24Hour = currentTimestamp + DAYS_1_IN_SECOND
        return timeMillis in currentTimestamp until next24Hour
    }

    /**
     * @param timeMillis timestamp in the past in milliseconds
     * @return description of [timeMillis] relative to current time
     */
    fun getRelativeTimeFromNow(timeMillis: Long, locale: Locale = localeID): String {
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

    /**
     * @param timeMillis timestamp in milliseconds
     * @return formatted date <dd MMM yyyy>
     */
    fun getDateMonthYearFormat(timeMillis: Long, locale: Locale = localeID): String {
        return format(timeMillis, "dd MMM yyyy", locale)
    }

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