package com.tokopedia.tokopoints.view.util

import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import com.tokopedia.tokopoints.view.model.merchantcoupon.AdInfo
import java.util.*

const val DEFAULT_TIME_STRING = "00 : 00 : 00"

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

fun isEventTriggered(context: Context, adInfo: AdInfo): Boolean {
    var check: Boolean? = false
    val setData = PersistentAdsData(context).getAdsSet()
    check = setData?.contains(adInfo?.AdID)
    return check!!
}


