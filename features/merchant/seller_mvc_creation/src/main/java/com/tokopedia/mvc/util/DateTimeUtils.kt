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
    private const val EXTRA_DAYS = 30
    const val PREVIOUS_EXTRA_DAYS = -30

    fun Context.getToday() = GregorianCalendar(LocaleUtils.getCurrentLocale(this))

    /**
     * Minimum end time should be 30 days before time
     */
    fun getMinDate(startCalendar: GregorianCalendar?): GregorianCalendar? =
        startCalendar?.let { startDate ->
            GregorianCalendar().apply {
                time = startDate.time
                add(Calendar.DATE, PREVIOUS_EXTRA_DAYS)
            }
        }

    /**
     * Maximum end date should be 30 days after date
     */
    fun getMaxDate(startCalendar: GregorianCalendar?): GregorianCalendar? =
        startCalendar?.let { startDate ->
            GregorianCalendar().apply {
                time = startDate.time
                add(Calendar.DATE, EXTRA_DAYS)
            }
        }
}

fun String.convertUnsafeDateTime(): Date {
    val locale = LocaleUtils.getIDLocale()
    return try {
        convertToDate(TIME_STAMP_FORMAT, locale)
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
