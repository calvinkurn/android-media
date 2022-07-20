package com.tokopedia.vouchercreation.common.extension

import com.tokopedia.vouchercreation.common.consts.LocaleConstant
import java.text.SimpleDateFormat
import java.util.*

private const val EMPTY_STRING = ""

fun Date.parseTo(desiredOutputFormat: String): String {
    return try {
        val outputFormat = SimpleDateFormat(desiredOutputFormat, LocaleConstant.INDONESIA)
        val output = outputFormat.format(this)
        output
    } catch (e: Exception) {
        EMPTY_STRING
    }
}

fun Date.toCalendar(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar
}
