package com.tokopedia.affiliate.utils

import android.content.Context
import com.tokopedia.affiliate.HOUR_PATTERN
import com.tokopedia.affiliate.NEW_DATE_FORMAT
import com.tokopedia.affiliate.PATTERN
import com.tokopedia.affiliate.SIX
import com.tokopedia.affiliate.THIRTY_THREE
import com.tokopedia.affiliate.TIME_ZONE
import com.tokopedia.affiliate.TWENTY_NINE
import com.tokopedia.affiliate.TWO
import com.tokopedia.affiliate.UTC
import com.tokopedia.affiliate.YYYY_MM_DD_T_HH_MM_SS_Z
import com.tokopedia.affiliate.ui.bottomsheet.AffiliateBottomDatePicker
import com.tokopedia.affiliate_toko.R
import com.tokopedia.kotlin.extensions.view.EMPTY
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    fun formatDate(
        currentFormat: String = YYYY_MM_DD_T_HH_MM_SS_Z,
        newFormat: String = NEW_DATE_FORMAT,
        dateString: String
    ): String {
        return try {
            val fromFormat: DateFormat = SimpleDateFormat(currentFormat, Locale.ENGLISH)
            fromFormat.isLenient = false
            fromFormat.timeZone = TimeZone.getTimeZone(UTC)
            val toFormat: DateFormat = SimpleDateFormat(newFormat, Locale.ENGLISH)
            toFormat.isLenient = false
            toFormat.timeZone = TimeZone.getDefault()

            val date = fromFormat.parse(dateString)
            date?.let { toFormat.format(it) } ?: String.EMPTY
        } catch (e: ParseException) {
            Timber.e(e, "cannot be parsed")
            dateString
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Pattern is invalid")
            dateString
        }
    }

    fun getMessage(dayRange: String, context: Context? = null): String {
        val timeZone = TimeZone.getTimeZone(TIME_ZONE)
        val calendar = Calendar.getInstance()
        when (dayRange) {
            AffiliateBottomDatePicker.TODAY -> {
                return try {
                    calendar.add(Calendar.HOUR, -TWO)
                    val date = SimpleDateFormat(PATTERN, Locale.ENGLISH).format(calendar.time)
                    val time = SimpleDateFormat(HOUR_PATTERN, Locale.ENGLISH).format(calendar.time)
                    "$date - ${context?.getString(R.string.terkhir_text)} $time"
                } catch (e: IllegalArgumentException) {
                    Timber.e(e, "Pattern is invalid")
                    String.EMPTY
                }
            }

            AffiliateBottomDatePicker.YESTERDAY -> {
                return try {
                    calendar.add(Calendar.HOUR, -THIRTY_THREE)
                    val dateFormat = SimpleDateFormat(PATTERN, Locale.ENGLISH)
                    dateFormat.timeZone = timeZone
                    dateFormat.format(calendar.time)
                } catch (e: IllegalArgumentException) {
                    Timber.e(e, "Pattern is invalid")
                    String.EMPTY
                }
            }

            AffiliateBottomDatePicker.SEVEN_DAYS -> {
                return try {
                    calendar.add(Calendar.HOUR, -THIRTY_THREE)
                    val lastDayFormat = SimpleDateFormat(PATTERN, Locale.ENGLISH)
                    lastDayFormat.timeZone = timeZone
                    val lastDay = lastDayFormat.format(calendar.time)
                    calendar.add(Calendar.DATE, -SIX)
                    val finalDayFormat = SimpleDateFormat(PATTERN, Locale.ENGLISH)
                    finalDayFormat.timeZone = timeZone
                    val finalDay = finalDayFormat.format(calendar.time)
                    "$finalDay - $lastDay"
                } catch (e: IllegalArgumentException) {
                    Timber.e(e, "Pattern is invalid")
                    String.EMPTY
                }
            }

            AffiliateBottomDatePicker.THIRTY_DAYS -> {
                return try {
                    calendar.add(Calendar.HOUR, -THIRTY_THREE)
                    val lastDayFormat = SimpleDateFormat(PATTERN, Locale.ENGLISH)
                    lastDayFormat.timeZone = timeZone
                    val lastDay = lastDayFormat.format(calendar.time)
                    calendar.add(Calendar.DATE, -TWENTY_NINE)
                    val finalDayFormat = SimpleDateFormat(PATTERN, Locale.ENGLISH)
                    finalDayFormat.timeZone = timeZone
                    val finalDay = finalDayFormat.format(calendar.time)
                    return "$finalDay - $lastDay"
                } catch (e: IllegalArgumentException) {
                    Timber.e(e, "Pattern is invalid")
                    String.EMPTY
                }
            }
        }
        return String.EMPTY
    }
}
