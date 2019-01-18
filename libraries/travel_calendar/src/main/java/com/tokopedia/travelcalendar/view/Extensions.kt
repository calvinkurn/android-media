package com.tokopedia.travelcalendar.view

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by nabillasabbaha on 08/01/19.
 */

const val CALENDAR_YYYYMMDD = "yyyy-MM-dd"
const val CALENDAR_MMMM = "MMMM"
const val CALENDAR_YYYY = "YYYY"
private val DEFAULT_LOCALE = Locale("in", "ID")

fun Resources.getColorCalendar(context: Context, @ColorRes resId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.getColor(resId, context.theme)
    } else {
        this.getColor(resId)
    }
}

fun Resources.getDrawableCalendar(context: Context, @DrawableRes resId: Int): Drawable {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.getDrawable(resId, context.theme)
    } else {
        this.getDrawable(resId)
    }
}

fun Calendar.getZeroTime(fcha: Date): Date {
    this.time = fcha;
    this.set(Calendar.HOUR_OF_DAY, 0)
    this.set(Calendar.MINUTE, 0)
    this.set(Calendar.SECOND, 0)
    this.set(Calendar.MILLISECOND, 0)

    val res = this.time
    return res
}

fun String.convertDate(format: String): Date {
    return SimpleDateFormat(format, DEFAULT_LOCALE).parse(this)
}

fun Date.convertString(format: String): String {
    return SimpleDateFormat(format, DEFAULT_LOCALE).format(this)
}
