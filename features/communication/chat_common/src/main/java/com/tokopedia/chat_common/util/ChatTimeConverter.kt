package com.tokopedia.chat_common.util

import android.content.Context
import android.text.format.DateUtils
import com.tokopedia.chat_common.R
import java.text.SimpleDateFormat
import java.util.*

object ChatTimeConverter {

    private const val TODAY = "Hari ini"
    private const val YESTERDAY = "Kemarin"

    fun unixToCalendar(unixTime: Long): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = unixTime
        return calendar
    }

    fun formatdiff(unixTime: Long): String {
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

    @JvmStatic
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

    fun getRelativeDate(context: Context, unixTime: Long): String {
        val diff = Calendar.getInstance().timeInMillis / 1000 - unixTime
        val status: String
        val minuteDivider: Long = 60
        val hourDivider = minuteDivider * 60
        val dayDivider = hourDivider * 24
        val monthDivider = dayDivider * 30
        when {
            diff / monthDivider > 0 -> {
                status = context.getString(R.string.topchat_online_months_ago, diff / monthDivider)
            }
            diff / dayDivider > 0 -> {
                status = context.getString(R.string.topchat_online_days_ago, diff / dayDivider)
            }
            diff / hourDivider > 0 -> {
                status = context.getString(
                    R.string.topchat_online_hours_ago,
                    diff / hourDivider
                )
            }
            else -> {
                var minutes = diff / minuteDivider
                if (minutes <= 0) minutes = 1
                status = context.getString(R.string.topchat_online_minutes_ago, minutes)
            }
        }
        return status
    }
}