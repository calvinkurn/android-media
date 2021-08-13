package com.tokopedia.vouchercreation.common.utils

import android.content.Context
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.kotlin.extensions.convertToDate
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
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

    const val DASH_DATE_FORMAT = "yyyy-MM-dd"
    const val DATE_FORMAT = "dd MMM yyyy"
    const val HOUR_FORMAT = "HH:mm"

    const val EXTRA_HOUR = 3
    const val EXTRA_MINUTE = 30
    const val EXTRA_WEEK = 7
    const val EXTRA_DAYS = 30
    const val MINUTE_INTERVAL = 30


    private const val DISPLAYED_DATE_FORMAT = "dd MMM yyyy"
    private const val RAW_DATE_FORMAT = "yyyy-MM-dd"

    private val rawDateFormatter by lazy {
        SimpleDateFormat(RAW_DATE_FORMAT, LocaleUtils.getIDLocale())
    }
    private val displayedDateFormatter by lazy {
        SimpleDateFormat(DISPLAYED_DATE_FORMAT, LocaleUtils.getIDLocale())
    }

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

    fun convertUnsafeDateTime(dateTime: String) : Date {
        val locale = Locale.getDefault()
        val firstFormatter = SimpleDateFormat(TIME_STAMP_FORMAT, locale)
        val secondFormatter = SimpleDateFormat(TIME_STAMP_MILLISECONDS_FORMAT, locale)

        return try {
            firstFormatter.parse(dateTime)
        } catch (ex: ParseException) {
            try {
                secondFormatter.parse(dateTime)
            } catch (ex: ParseException) {
                ex.printStackTrace()
                throw RuntimeException(ex.message)
            }
        }
    }

    fun convertFullDateToDateParam(date: String, newFormat: String): String {
        return try {
            reformatDateTime(date, TIME_STAMP_FORMAT, newFormat)
        } catch (ex: ParseException) {
            try {
                reformatDateTime(date, TIME_STAMP_MILLISECONDS_FORMAT, newFormat)
            } catch (ex: ParseException) {
                Timber.e(ex)
                throw RuntimeException()
            }
        }
    }

    fun Context.getToday() = GregorianCalendar(LocaleUtils.getCurrentLocale(this))

    /**
     * Minimum start time should be 3 hours after current time
     */
    fun Context.getMinStartDate() =
            getToday().apply {
                add(Calendar.HOUR, EXTRA_HOUR)
            }

    /**
     * Maximum start date should 30 days after current time
     */
    fun Context.getMaxStartDate() =
            getToday().apply {
                add(Calendar.DATE, EXTRA_DAYS)
            }

    /**
     * Minimum end time should be 30 minutes after starting time
     */
    fun getMinEndDate(startCalendar: GregorianCalendar?) : GregorianCalendar? =
            startCalendar?.let { startDate ->
                GregorianCalendar().apply {
                    time = startDate.time
                    add(Calendar.MINUTE, EXTRA_MINUTE)
                }
            }

    /**
     * Maximum end date should be 30 days after start date
     */
    fun getMaxEndDate(startCalendar: GregorianCalendar?): GregorianCalendar? =
            startCalendar?.let { startDate ->
                GregorianCalendar().apply {
                    time = startDate.time
                    add(Calendar.DATE, EXTRA_DAYS)
                }
            }

    internal fun getDisplayedDateString(context: Context?,
                                        startDate: String,
                                        startHour: String,
                                        endDate: String,
                                        endHour: String): String {
        val formattedStartDate = startDate.formatToDisplayedDate()
        val formattedEndDate = endDate.formatToDisplayedDate()
        return String.format(context?.getString(R.string.mvc_displayed_date_format).toBlankOrString(),
                formattedStartDate, startHour, formattedEndDate, endHour)
    }

    internal fun getDisplayedDateString(context: Context?,
                                        startDate: String,
                                        endDate: String): String {
        val formattedStartDate = startDate.formatToDisplayedDate()
        val formattedEndDate = endDate.formatToDisplayedDate()
        return String.format(context?.getString(R.string.mvc_displayed_date_post_format).toBlankOrString(),
                formattedStartDate, formattedEndDate)
    }

    private fun String.formatToDisplayedDate(): String {
        try {
            rawDateFormatter.parse(this)?.let {
                return displayedDateFormatter.format(it)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ""
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
