package com.tokopedia.common.travel.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by nabillasabbaha on 19/11/18.
 */
class TravelDateUtil {
    companion object {
        private val DEFAULT_TIMEZONE: TimeZone = TimeZone.getTimeZone("GMT+7")
        private val DEFAULT_LOCALE = Locale("in", "ID")

        const val DEFAULT_VIEW_FORMAT = "dd MMM yyyy"
        const val DEFAULT_VIEW_TIME_FORMAT = "dd MMM yyyy, HH:mm"
        const val YYYY_MM_DD = "yyyy-MM-dd"
        const val FORMAT_DATE = "EEEE, dd LLLL yyyy"
        const val YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss"
        const val VIEW_FORMAT_WITHOUT_YEAR = "dd MMM"
        const val YYYYMMDD = "yyyyMMdd"
        const val HH_MM = "HH:mm"
        const val EEE_DD_MMM_YY = "EEE, dd MMM yy"

        fun stringToDate(format: String, input: String): Date {
            val fromFormat: DateFormat = SimpleDateFormat(format, DEFAULT_LOCALE)
            return try {
                fromFormat.parse(input) ?: throw ParseException("Failed to parse", 0)
            } catch (e: ParseException) {
                e.printStackTrace()
                throw RuntimeException("Date doesnt valid ($input) with format$format")
            }
        }

        fun dateToString(format: String, input: Date): String {
            val formatDate: DateFormat = SimpleDateFormat(format, DEFAULT_LOCALE)
            return formatDate.format(input)
        }

        fun getCurrentCalendar(): Calendar = Calendar.getInstance()

        fun addTimeToSpesificDate(date: Date, field: Int, value: Int): Date {
            val now = getCurrentCalendar()
            now.time = date
            now.add(field, value)
            return now.time
        }

        fun removeTime(date: Date): Date {
            val cal = Calendar.getInstance()
            cal.time = date
            cal[Calendar.HOUR_OF_DAY] = 0
            cal[Calendar.MINUTE] = 0
            cal[Calendar.SECOND] = 0
            cal[Calendar.MILLISECOND] = 0
            return cal.time
        }

        @JvmStatic
        fun formatDateByUsersTimezone(currentFormat: String, newFormat: String, dateString: String): String {
            val timeZone = TimeZone.getDefault()
            return formatDate(currentFormat, newFormat, dateString, DEFAULT_LOCALE, DEFAULT_TIMEZONE, timeZone)
        }

        @JvmOverloads
        fun formatDate(currentFormat: String, newFormat: String, dateString: String, locale: Locale = DEFAULT_LOCALE): String {
            return try {
                val fromFormat: DateFormat = SimpleDateFormat(currentFormat, locale)
                fromFormat.isLenient = false
                val toFormat: DateFormat = SimpleDateFormat(newFormat, locale)
                toFormat.isLenient = false
                val date = fromFormat.parse(dateString)
                if (date != null) {
                    return toFormat.format(date)
                } else {
                    ""
                }
            } catch (e: ParseException) {
                e.printStackTrace()
                dateString
            }
        }

        fun formatDate(currentFormat: String,
                       newFormat: String,
                       dateString: String,
                       locale: Locale,
                       fromTimeZone: TimeZone,
                       toTimezone: TimeZone): String {
            return try {
                val fromFormat: DateFormat = SimpleDateFormat(currentFormat, locale)
                fromFormat.timeZone = fromTimeZone
                fromFormat.isLenient = false
                val toFormat: DateFormat = SimpleDateFormat(newFormat, locale)
                toFormat.isLenient = false
                toFormat.timeZone = toTimezone
                val date = fromFormat.parse(dateString)
                if (date != null) {
                    toFormat.format(date)
                } else {
                    ""
                }
            } catch (e: ParseException) {
                e.printStackTrace()
                dateString
            }
        }

        @JvmStatic
        fun formatToUi(dateStr: String): String {
            return formatDate(YYYY_MM_DD, DEFAULT_VIEW_FORMAT, dateStr)
        }
    }
}