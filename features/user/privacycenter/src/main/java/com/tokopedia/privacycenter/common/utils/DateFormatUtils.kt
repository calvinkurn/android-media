package com.tokopedia.privacycenter.common.utils

import java.text.SimpleDateFormat
import java.util.*

private const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
private const val TIMEZONE_UTC = "UTC"
private const val LANGUAGE_CODE = "id"
private const val COUNTRY_CODE = "ID"
private const val LOCAL_DATE_FORMAT = "dd MMM yyyy"

fun String.formatDateLocalTimezone(): String {
    return try {
        val date = SimpleDateFormat(SERVER_DATE_FORMAT, getIndonesiaLocale())
        date.timeZone = TimeZone.getTimeZone(TIMEZONE_UTC)
        SimpleDateFormat(
            LOCAL_DATE_FORMAT,
            getIndonesiaLocale()
        ).format(date.parse(this) ?: "")
    } catch (_: Exception) {
        this
    }
}

private fun getIndonesiaLocale(): Locale {
    return try {
        Locale(LANGUAGE_CODE, COUNTRY_CODE)
    } catch (_: Exception) {
        Locale.getDefault()
    }
}
