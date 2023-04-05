package com.tokopedia.shop_nib.util.extension

import java.util.Date
import java.util.Calendar


fun Date.extractHour(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun Date.extractMinute(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MINUTE)
}
