package com.tokopedia.tokopoints.view.util

import android.content.Context
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

fun convertLongToHourMinuteSec(l : Long) : String {
    val seconds = (l / 1000).toInt() % 60
    val minutes = (l / (1000 * 60) % 60).toInt()
    val hours = (l / (1000 * 60 * 60) % 24).toInt()
    return String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds)
}

fun convertDpToPixel(dp: Int, context: Context): Int {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}