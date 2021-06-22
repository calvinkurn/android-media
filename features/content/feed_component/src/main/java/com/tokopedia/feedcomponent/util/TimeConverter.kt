package com.tokopedia.feedcomponent.util

import android.content.Context
import com.tokopedia.feedcomponent.R
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author by nisie on 5/17/17.
 */

object TimeConverter {
    private const val SECONDS_IN_MINUTE: Long = 60
    private const val MINUTES_IN_HOUR = (60 * 60).toLong()
    private const val HOUR_IN_A_DAY = (60 * 60 * 24).toLong()
    private const val DEFAULT_FEED_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    private const val HOUR_MINUTE_FORMAT = "HH:mm"
    private const val DAY_MONTH_FORMAT = "dd MMMM"
    private const val DAY_MONTH_YEAR_FORMAT = "dd MMMM yyyy"
    private const val MONTH_YEAR_FORMAT = "MMMM yyyy"
    private const val COUNTRY_ID = "ID"
    private const val LANGUAGE_ID = "in"

    fun generateTime(context: Context, postTime: String): String {
        return generateTime(context, postTime, DEFAULT_FEED_FORMAT)
    }

    private fun generateTime(context: Context, postTime: String, format: String): String {
        return try {
            val localeID = Locale(LANGUAGE_ID, COUNTRY_ID)
            val sdf = SimpleDateFormat(format, localeID)
            sdf.timeZone = TimeZone.getDefault()
            val postDate = sdf.parse(postTime)
            getFormattedTime(context, postDate)

        } catch (e: ParseException) {
            Timber.d(e)
            postTime
        }

    }

    fun generateTimeNew(
        context: Context,
        postTime: String
    ): String {
        return try {
            val localeID = Locale(LANGUAGE_ID, COUNTRY_ID)
            val sdf = SimpleDateFormat(DEFAULT_FEED_FORMAT, localeID)
            sdf.timeZone = TimeZone.getDefault()
            val postDate = sdf.parse(postTime)
            getFormattedTimeNew(context, postDate)

        } catch (e: ParseException) {
            Timber.d(e)
            postTime
        }
    }

    private fun getFormattedTimeNew(context: Context, postDate: Date): String {
        val localeID = Locale(LANGUAGE_ID, COUNTRY_ID)
        val currentTime = Date()

        val calPostDate = Calendar.getInstance()
        calPostDate.time = postDate

        val calCurrentTime = Calendar.getInstance()
        calCurrentTime.time = currentTime

        val sdfDay = SimpleDateFormat(DAY_MONTH_FORMAT, localeID)
        val monthYear = SimpleDateFormat(MONTH_YEAR_FORMAT, localeID)

        /*less than a minute*/
        return if (getDifference(currentTime, postDate) < 60) {
            context.getString(R.string.post_time_just_now_new)

            /*less than 1 hour*/
        } else if (getDifference(currentTime, postDate) / SECONDS_IN_MINUTE < 60) {
            (getDifference(currentTime, postDate) / SECONDS_IN_MINUTE).toString().plus(" ")
                .plus(context.getString(R.string.post_time_minutes_ago_new))

            /*less than 1 day*/
        } else if (getDifference(currentTime, postDate) / MINUTES_IN_HOUR < 24) {
            (getDifference(currentTime, postDate) / MINUTES_IN_HOUR).toString().plus(" ")
                .plus(context.getString(R.string.post_time_hours_ago_new))

            /*less than 7 day*/
        } else if (getDifference(currentTime, postDate) / MINUTES_IN_HOUR < (24 * 7)) {
            (getDifference(currentTime, postDate) / (MINUTES_IN_HOUR * 24)).toString().plus(" ")
                .plus(context.getString(R.string.post_time_days_ago))

            /*less than 1 year*/
        } else if (getDifference(currentTime, postDate) / HOUR_IN_A_DAY < 365) {
            sdfDay.format(postDate)
        } else {
            monthYear.format(postDate)
        }
    }

    private fun getFormattedTime(context: Context, postDate: Date): String {
        val localeID = Locale(LANGUAGE_ID, COUNTRY_ID)
        val currentTime = Date()

        val calPostDate = Calendar.getInstance()
        calPostDate.time = postDate

        val calCurrentTime = Calendar.getInstance()
        calCurrentTime.time = currentTime

        val sdfHour = SimpleDateFormat(HOUR_MINUTE_FORMAT, localeID)
        val sdfDay = SimpleDateFormat(DAY_MONTH_FORMAT, localeID)
        val sdfYear = SimpleDateFormat(DAY_MONTH_YEAR_FORMAT, localeID)

        return if (getDifference(currentTime, postDate) < 60) {
            context.getString(R.string.post_time_just_now)

        } else if (getDifference(currentTime, postDate) / SECONDS_IN_MINUTE < 60) {
            (getDifference(currentTime, postDate) / SECONDS_IN_MINUTE).toString()
                .plus(context.getString(R.string.post_time_minutes_ago))

        } else if (getDifference(currentTime, postDate) / MINUTES_IN_HOUR < 24
            && calCurrentTime.get(Calendar.DAY_OF_MONTH) == calPostDate.get(Calendar.DAY_OF_MONTH)
            && calCurrentTime.get(Calendar.MONTH) == calPostDate.get(Calendar.MONTH)
            && calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)
        ) {
            (getDifference(currentTime, postDate) / MINUTES_IN_HOUR).toString()
                .plus(context.getString(R.string.post_time_hours_ago))

        } else if (calCurrentTime.get(Calendar.MONTH) == calPostDate.get(Calendar.MONTH)
            && calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR)
            && isYesterday(
                calCurrentTime.get(Calendar.DAY_OF_MONTH),
                calPostDate.get(Calendar.DAY_OF_MONTH)
            )
        ) {
            context.getString(R.string.post_time_yesterday_at)
                .plus(sdfHour.format(postDate))

        } else if (calCurrentTime.get(Calendar.YEAR) == calPostDate.get(Calendar.YEAR))
            sdfDay.format(postDate)
                .plus(context.getString(R.string.post_time_hour))
                .plus(sdfHour.format(postDate))
        else {
            sdfYear.format(postDate)
        }
    }

    private fun isYesterday(currentDay: Int, postDay: Int): Boolean {
        return currentDay - postDay == 1
    }

    private fun getDifference(currentTime: Date, postDate: Date): Long {
        return (currentTime.time - postDate.time) / 1000
    }
}
