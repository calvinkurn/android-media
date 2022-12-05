package com.tokopedia.mvc.util

import android.content.Context
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.kotlin.extensions.convertToDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.mvc.util.DateTimeUtils.FULL_DAY_FORMAT
import com.tokopedia.mvc.util.DateTimeUtils.TIME_STAMP_FORMAT
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By @ilhamsuaib on 12/05/20
 */

object DateTimeUtils {

    const val TIME_STAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val TIME_STAMP_MILLISECONDS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
    const val FULL_DAY_FORMAT = "EEE, dd MMM yyyy, HH:mm"

    const val DASH_DATE_FORMAT = "yyyy-MM-dd"
    const val DATE_FORMAT = "dd MMM yyyy"
    const val DATE_FORMAT_DAY_MONTH = "dd MMM"
    const val HOUR_FORMAT = "HH:mm"

    const val EXTRA_HOUR = 3
    const val EXTRA_MINUTE = 30
    const val EXTRA_WEEK = 7
    const val EXTRA_DAYS = 30
    const val EXTRA_DAYS_COUPON = 31
    const val MINUTE_INTERVAL = 30
    const val ROLLOUT_DATE_THRESHOLD_TIME = 1647536401000L

    private const val DISPLAYED_DATE_FORMAT = "dd MMM yyyy"
    private const val RAW_DATE_FORMAT = "yyyy-MM-dd"

    fun Context.getToday() = GregorianCalendar(LocaleUtils.getCurrentLocale(this))

    /**
     * Maximum start date should 30 days after current time
     */
    fun Context.getMaxStartDate() =
        getToday().apply {
            add(Calendar.DATE, EXTRA_DAYS)
        }
}

fun String.convertUnsafeDateTime(): Date {
    val locale = LocaleUtils.getIDLocale()
    return try {
        convertToDate(DateTimeUtils.TIME_STAMP_FORMAT, locale)
    } catch (ex: ParseException) {
        try {
            convertToDate(DateTimeUtils.TIME_STAMP_MILLISECONDS_FORMAT, locale)
        } catch (ex: ParseException) {
            Timber.e(ex)
            throw RuntimeException()
        }
    }
}

fun Date.formatTo(
    desiredOutputFormat: String,
    locale: Locale = Locale("id", "ID")
): String {
    return try {
        val outputFormat = SimpleDateFormat(desiredOutputFormat, locale)
        //   outputFormat.timeZone = timeZone
        val output = outputFormat.format(this)
        output
    } catch (e: Exception) {
        ""
    }
}

fun Date.formatTo(locale: Locale): String {
    return this.toFormattedString(TIME_STAMP_FORMAT).convertDate(locale)
}

private fun String.convertDate(locale: Locale): String {
    return this.convertUnsafeDateTime().formatTo(FULL_DAY_FORMAT, locale).toBlankOrString()
}
