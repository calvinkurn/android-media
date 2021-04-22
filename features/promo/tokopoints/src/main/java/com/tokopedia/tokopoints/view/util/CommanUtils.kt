package com.tokopedia.tokopoints.view.util

import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.View
import com.tokopedia.unifyprinciples.Typography
import java.util.*

private val LIST_TAG_START = "<li>"
private val LIST_TAG_END = "</li>"
private val MAX_POINTS_TO_SHOW = 4
const val DEFAULT_TIME_STRING = "00 : 00 : 00"

fun getLessDisplayData(data: String, seeMore: Typography): String {
    val totalString = data.split(LIST_TAG_START.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
    var displayString = totalString[0] + LIST_TAG_START
    if (totalString.size > MAX_POINTS_TO_SHOW + 1) {
        for (i in 1 until MAX_POINTS_TO_SHOW) {
            displayString = displayString + (totalString[i] + LIST_TAG_START)
        }
        displayString = displayString + totalString[MAX_POINTS_TO_SHOW]
        val lastString = totalString[totalString.size - 1]
        if (lastString.contains(LIST_TAG_END)) {
            displayString = displayString + LIST_TAG_END + totalString[totalString.size - 1].split(LIST_TAG_END.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
        }
    } else {
        displayString = data
        seeMore.visibility = View.GONE
    }
    return displayString
}

fun convertLongToHourMinuteSec(l: Long): Triple<Int, Int, Int> {
    val seconds = (l / 1000).toInt() % 60
    val minutes = (l / (1000 * 60) % 60).toInt()
    val hours = (l / (1000 * 60 * 60) % 24).toInt()
    return Triple(hours, minutes, seconds)
}

fun convertSecondsToHrMmSs(timerValue: Long): Calendar {
    val seconds = timerValue.rem(60)
    val minutes = (timerValue.rem((60 * 60))).div(60)
    val hours = timerValue.div((60 * 60))
    val cal = Calendar.getInstance()
    cal.add(Calendar.HOUR, hours.toInt())
    cal.add(Calendar.MINUTE, minutes.toInt())
    cal.add(Calendar.SECOND, seconds.toInt())

    return cal
}

fun convertDpToPixel(dp: Int, context: Context): Int {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

fun isDarkMode(context: Context): Boolean {
    return try {
        when (context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    } catch (ignored: Exception) {
        false
    }
}



