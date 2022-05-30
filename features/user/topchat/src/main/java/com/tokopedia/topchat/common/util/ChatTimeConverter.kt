package com.tokopedia.topchat.common.util

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by stevenfredian on 9/19/17.
 */
object ChatTimeConverter {
    fun unixToCalendar(unixTime: Long): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = unixTime
        return calendar
    }

    fun formatTimeStamp(unixTime: Long): String {
        val localeID = Locale("in", "ID")
        val postTime = Date(unixTime)
        val calPostDate = Calendar.getInstance()
        calPostDate.time = postTime
        val calCurrentTime = Calendar.getInstance()
        calCurrentTime.time = Date()
        return if (DateUtils.isToday(unixTime)) {
            val sdfHour = SimpleDateFormat("HH:mm", localeID)
            sdfHour.format(postTime)
        } else if (calCurrentTime[Calendar.MONTH] == calPostDate[Calendar.MONTH] && calCurrentTime[Calendar.YEAR] == calPostDate[Calendar.YEAR] && isYesterday(
                calCurrentTime[Calendar.DAY_OF_MONTH],
                calPostDate[Calendar.DAY_OF_MONTH]
            )
        ) {
            YESTERDAY
        } else if (calCurrentTime[Calendar.YEAR] == calPostDate[Calendar.YEAR]) {
            val sdfDay = SimpleDateFormat("dd MMM", localeID)
            sdfDay.format(postTime)
        } else {
            val sdfYear = SimpleDateFormat("dd MMM yyyy", localeID)
            sdfYear.format(postTime)
        }
    }

    private fun isYesterday(currentDay: Int, postDay: Int): Boolean {
        return currentDay - postDay == 1
    }

    fun formatTime(unixTime: Long): String {
        val localeID = Locale("in", "ID")
        val postTime = Date(unixTime)
        val sdfHour = SimpleDateFormat("HH:mm", localeID)
        return sdfHour.format(postTime)
    }

    fun formatFullTime(unixTime: Long): String {
        val localeID = Locale("in", "ID")
        val postTime = Date(unixTime)
        val calPostDate = Calendar.getInstance()
        calPostDate.time = postTime
        val calCurrentTime = Calendar.getInstance()
        calCurrentTime.time = Date()
        return if (DateUtils.isToday(unixTime)) {
            val sdfHour = SimpleDateFormat("HH:mm", localeID)
            String.format(
                "%s, %s",
                TODAY,
                sdfHour.format(postTime)
            )
        } else if (calCurrentTime[Calendar.MONTH] == calPostDate[Calendar.MONTH] && calCurrentTime[Calendar.YEAR] == calPostDate[Calendar.YEAR] && isYesterday(
                calCurrentTime[Calendar.DAY_OF_MONTH],
                calPostDate[Calendar.DAY_OF_MONTH]
            )
        ) {
            val sdfHour = SimpleDateFormat("HH:mm", localeID)
            String.format(
                "%s, %s",
                YESTERDAY,
                sdfHour.format(postTime)
            )
        } else if (calCurrentTime[Calendar.YEAR] == calPostDate[Calendar.YEAR]) {
            val sdfDay = SimpleDateFormat("dd MMM, HH:mm", localeID)
            sdfDay.format(postTime)
        } else {
            val sdfYear = SimpleDateFormat("dd MMM yyyy, HH:mm", localeID)
            sdfYear.format(postTime)
        }
    }

    private const val TODAY = "Hari ini"
    private const val YESTERDAY = "Kemarin"
}