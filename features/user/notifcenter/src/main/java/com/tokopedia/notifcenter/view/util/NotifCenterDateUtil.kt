package com.tokopedia.notifcenter.view.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.notifcenter.R
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * @author by milhamj on 31/08/18.
 */

class NotifCenterDateUtil @Inject constructor(@ApplicationContext val context: Context) {

    companion object {
        private const val millisMultiplier = 1000L
        private const val DATE_FORMAT = "d MMMM"
        private const val COUNTRY_ID = "ID"
        private const val LANGUAGE_ID = "id"
    }

    fun getPrettyDate(timeInUnix: Int): String {
        val timeInMillis = timeInUnix * millisMultiplier
        val timeInCalendar: Calendar = Calendar.getInstance()
        timeInCalendar.timeInMillis = timeInMillis

        if (isToday(timeInCalendar)) {
            return context.getString(R.string.notif_today)
        } else if (isYesterday(timeInCalendar)) {
            return context.getString(R.string.notif_yesterday)
        } else {
            val localeID = Locale(LANGUAGE_ID, COUNTRY_ID)
            val sdf = SimpleDateFormat(DATE_FORMAT, localeID)
            val date = Date(timeInMillis)
            return sdf.format(date)
        }
    }

    private fun isToday(calendar: Calendar): Boolean {
        return isDateTheSame(calendar, 0)
    }

    private fun isYesterday(calendar: Calendar): Boolean {
        return isDateTheSame(calendar, -1)
    }

    private fun isDateTheSame(calendar: Calendar, amountOfDayToAdd: Int): Boolean {
        val dateCompared: Calendar = Calendar.getInstance()
        dateCompared.add(Calendar.DAY_OF_YEAR, amountOfDayToAdd)

        return (dateCompared.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && dateCompared.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR))
    }
}
