package com.tokopedia.inboxcommon.time

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TimeHelper {

    private val DAYS_90_IN_SECOND = TimeUnit.DAYS.toSeconds(90)
    private val DAYS_6_IN_SECOND = TimeUnit.DAYS.toSeconds(6)
    private val DAYS_1_IN_SECOND = TimeUnit.DAYS.toSeconds(1)
    private val HOUR_1_IN_SECOND = TimeUnit.HOURS.toSeconds(1)
    private val MINUTE_1_IN_SECOND = TimeUnit.MINUTES.toSeconds(1)

    fun getRelativeTimeFromNow(timestamp: Long): String {
        val diff: Long = Calendar.getInstance().timeInMillis / 1000 - timestamp
        return when {
            diff / DAYS_90_IN_SECOND > 0 -> getMonthYearFormat(timestamp * 1000)
            diff / DAYS_6_IN_SECOND > 0 -> getDateMonthFormat(timestamp * 1000)
            diff / DAYS_1_IN_SECOND > 0 -> "${diff / DAYS_1_IN_SECOND} hari lalu"
            diff / HOUR_1_IN_SECOND > 0 -> "${diff / HOUR_1_IN_SECOND} jam"
            diff / MINUTE_1_IN_SECOND > 0 -> "${diff / MINUTE_1_IN_SECOND} mnt"
            else -> "<1 mnt"
        }
    }

    /**
     * @param timestamp timestamp in milliseconds
     * @return formatted date <MMM yyyy>
     */
    private fun getMonthYearFormat(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        return format.format(date)
    }

    /**
     * @param timestamp timestamp in milliseconds
     * @return formatted date <dd MMM>
     */
    private fun getDateMonthFormat(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd MMM", Locale.getDefault())
        return format.format(date)
    }

}