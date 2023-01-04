package com.tokopedia.mvc.util

import com.tokopedia.campaign.utils.constant.DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.kotlin.extensions.convertToDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.mvc.util.DateTimeUtils.FULL_DAY_FORMAT
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By @ilhamsuaib on 12/05/20
 */

object DateTimeUtils {

    const val TIME_STAMP_MILLISECONDS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
    const val FULL_DAY_FORMAT = "EEE, dd MMM yyyy, HH:mm"
    const val DASH_DATE_FORMAT = "yyyy-MM-dd"
    private const val EXTRA_MONTH = 1
    private const val PREVIOUS_EXTRA_MONTH = -1
    const val DATE_FORMAT = "dd MMM yyyy"
    const val HOUR_FORMAT = "HH:mm"

    /**
     * Minimum end time should be 30 days before time
     */
    fun getMinDate(startCalendar: GregorianCalendar?): GregorianCalendar? =
        startCalendar?.let { startDate ->
            GregorianCalendar().apply {
                time = startDate.time
                add(Calendar.MONTH, PREVIOUS_EXTRA_MONTH)
            }
        }

    /**
     * Maximum end date should be 30 days after date
     */
    fun getMaxDate(startCalendar: GregorianCalendar?): GregorianCalendar? =
        startCalendar?.let { startDate ->
            GregorianCalendar().apply {
                time = startDate.time
                add(Calendar.MONTH, EXTRA_MONTH)
            }
        }
}

fun String.convertUnsafeDateTime(): Date {
    val locale = LocaleUtils.getIDLocale()
    return try {
        convertToDate(DATE_WITH_SECOND_PRECISION_ISO_8601, locale)
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
    locale: Locale = LocaleUtils.getIDLocale()
): String {
    return try {
        val outputFormat = SimpleDateFormat(desiredOutputFormat, locale)
        val output = outputFormat.format(this)
        output
    } catch (e: Exception) {
        ""
    }
}

fun Date.formatTo(locale: Locale = LocaleUtils.getIDLocale()): String {
    return this.toFormattedString(DATE_WITH_SECOND_PRECISION_ISO_8601).convertDate(FULL_DAY_FORMAT, locale)
}

fun String.convertDate(format: String, locale: Locale = LocaleUtils.getIDLocale()): String {
    return this.convertUnsafeDateTime().formatTo(format, locale).toBlankOrString()
}
