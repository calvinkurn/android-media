package com.tokopedia.review.feature.reputationhistory.util

import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.kotlin.extensions.view.orZero
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

object ReputationPenaltyDateUtils {
    fun format(timeMillis: Long, pattern: String): String {
        val sdf = SimpleDateFormat(pattern, DateFormatUtils.DEFAULT_LOCALE)
        return sdf.format(timeMillis)
    }

    fun getPastDaysReputationPenaltyDate(totalDays: Int): Date {
        val date = Calendar.getInstance(DateFormatUtils.DEFAULT_LOCALE)
        date.set(Calendar.DAY_OF_YEAR, date.get(Calendar.DAY_OF_YEAR) - totalDays)
        return date.time
    }

    fun String.convertDateTextToTimeStamp(patternDate: String): Long {
        return try {
            val sdf = SimpleDateFormat(patternDate, DateFormatUtils.DEFAULT_LOCALE)
            val dateParse = sdf.parse(this)
            return dateParse?.time.orZero()
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun isTheSameYear(startDate: Long, endDate: Long): Boolean {
        return try {
            return startDate.getCalendar() == endDate.getCalendar()
        } catch (e: Exception) {
            false
        }
    }

    private fun Long.getCalendar(): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return calendar.get(Calendar.YEAR)
    }

}