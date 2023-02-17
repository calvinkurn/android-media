package com.tokopedia.universal_sharing.util

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtil {

    private const val ONE_THOUSAND = 1000
    private const val HOUR_FORMAT = "HH:mm"
    private const val DATE_UPCOMING_FORMAT = "dd MM yyyy, jam HH:mm WIB"
    fun timeIsUnder1Day(date: Long): Boolean {
        return try {
            val endDateMillis = date * ONE_THOUSAND
            val endDate = Date(endDateMillis)
            val now = System.currentTimeMillis()
            val diff = (endDate.time - now).toFloat()
            if (diff < 0) {
                //End date is out dated
                false
            } else {
                TimeUnit.MILLISECONDS.toDays(endDate.time - now) < 1
            }
        } catch (e: Throwable) {
            false
        }
    }

    fun timeIsUnderThresholdMinute(date: Long, threshold: Long): Boolean {
        return try {
            val endDateMillis = date * ONE_THOUSAND
            val endDate = Date(endDateMillis)
            val now = System.currentTimeMillis()
            val diff = (endDate.time - now).toFloat()
            if (diff < 0) {
                //End date is out dated
                false
            } else {
                TimeUnit.MILLISECONDS.toMinutes(endDate.time - now) < threshold
            }
        } catch (e: Throwable) {
            false
        }
    }

    /**
     * @param date in unix time
     * @return 0 if the date is outdated or error
     * @return > 0 if still have minutes left
     */
    fun getMinuteLeft(date: Long): Long {
        return try {
            val endDateMillis = date * ONE_THOUSAND
            val endDate = Date(endDateMillis)
            val now = System.currentTimeMillis()
            val diff = (endDate.time - now).toFloat()
            if (diff < 0) {
                //End date is out dated
               0L
            } else {
                TimeUnit.MILLISECONDS.toMinutes(endDate.time - now)
            }
        } catch (e: Throwable) {
            0L
        }
    }

    fun getHour(date: Long): String {
        val dateTime = Date(date * ONE_THOUSAND)
        val formatter = SimpleDateFormat(HOUR_FORMAT, Locale.getDefault())
        return formatter.format(dateTime)
    }

    fun getDateUpcoming(date: Long): String {
        val dateTime = Date(date * ONE_THOUSAND)
        val formatter = SimpleDateFormat(DATE_UPCOMING_FORMAT, Locale.getDefault())
        return formatter.format(dateTime)
    }
}
