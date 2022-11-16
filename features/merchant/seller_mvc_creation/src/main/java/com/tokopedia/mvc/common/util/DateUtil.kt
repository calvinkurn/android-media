package com.tokopedia.mvc.common.util

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    private const val TIME_STAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val TIME_STAMP_MILLISECONDS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
    private const val DATETIME_FORMAT = "dd MMM yyyy"
    private const val DATE_RANGE_SEPARATOR = " - "

    fun formatUnsafeDateTime(dateTime: String, newFormat: String): String {
        val locale = Locale.getDefault()
        val firstFormatter = SimpleDateFormat(TIME_STAMP_FORMAT, locale)
        val secondFormatter = SimpleDateFormat(TIME_STAMP_MILLISECONDS_FORMAT, locale)
        val newFormatter = SimpleDateFormat(newFormat, locale)
        try {
            return newFormatter.format(firstFormatter.parse(dateTime))
        } catch (ex: ParseException) {
            try {
                return newFormatter.format(secondFormatter.parse(dateTime))
            } catch (ex: ParseException) {
                Timber.e(ex)
            }
        }
        return dateTime
    }

    fun String.formatToDate() = formatUnsafeDateTime(this, DATETIME_FORMAT)

    fun formatStartFinishDate(startDate: String, endDate: String) =
        startDate.formatToDate() + DATE_RANGE_SEPARATOR + endDate.formatToDate()
}
