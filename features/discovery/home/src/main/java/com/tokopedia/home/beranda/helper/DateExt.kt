package com.tokopedia.home.beranda.helper

import java.util.*

/**
 * Created by Lukas on 2/5/21.
 */

fun Calendar.toStringFormat(): String{
    return String.format("%s:%s:%s", this.get(Calendar.HOUR).toString(), this.get(Calendar.MINUTE).toString(), this.get(Calendar.SECOND).toString())
}

fun Date.getTimeDiff(endTime: Date): Calendar {
    var diff = endTime.time - time
    if (diff < 0) diff = 0
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.HOUR, (diff / (60 * 60 * 1000) % 24).toInt())
    calendar.add(Calendar.MINUTE, (diff / (60 * 1000) % 60).toInt())
    calendar.add(Calendar.SECOND, (diff / 1000 % 60).toInt())
    return calendar
}