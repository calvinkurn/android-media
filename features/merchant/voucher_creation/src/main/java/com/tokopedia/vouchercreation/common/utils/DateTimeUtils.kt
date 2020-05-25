package com.tokopedia.vouchercreation.common.utils

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By @ilhamsuaib on 12/05/20
 */

object DateTimeUtils {

    private const val TIME_STAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    private const val TIME_STAMP_MILLISECONDS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"

    fun reformatDateTime(dateTime: String, oldFormat: String, newFormat: String): String {
        val locale = Locale.getDefault()
        val oldSdf = SimpleDateFormat(oldFormat, locale)
        val newSdf = SimpleDateFormat(newFormat, locale)
        return newSdf.format(oldSdf.parse(dateTime))
    }

    fun reformatUnsafeDateTime(dateTime: String, newFormat: String) : String {
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
}