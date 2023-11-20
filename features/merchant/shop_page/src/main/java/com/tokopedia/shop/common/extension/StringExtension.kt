package com.tokopedia.shop.common.extension

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(
    inputFormat: String,
    timeZone: TimeZone = TimeZone.getDefault(),
    locale: Locale = Locale("id", "ID")
): Date {
    return try {
        val format = SimpleDateFormat(inputFormat, locale)
        format.timeZone = timeZone
        format.parse(this) ?: Date()
    } catch (e: Exception) {
        Timber.e(e)
        Date()
    }
}
