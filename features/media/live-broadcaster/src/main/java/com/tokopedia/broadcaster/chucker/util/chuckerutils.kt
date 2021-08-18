package com.tokopedia.broadcaster.chucker.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.dateFormat(): String {
    if (this == 0L) return "Unknown"

    val format = "dd/MM/yy hh:mm"
    return SimpleDateFormat(
        format,
        Locale.US
    ).format(Date(this))
}