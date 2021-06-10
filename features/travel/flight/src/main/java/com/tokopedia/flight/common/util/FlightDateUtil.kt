package com.tokopedia.flight.common.util

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by alvarisi on 10/30/17.
 */
class FlightDateUtil {

    fun getDayDiff(timestamp: String): Long {
        val inputDate = trimDate(stringToDate(YYYY_MM_DD_T_HH_MM_SS_Z, timestamp))
        val currentDate = trimDate(currentDate)
        val diff = inputDate.time - currentDate.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }

    companion object {

        const val FORMAT_DATE = "EEEE, dd LLLL yyyy"
        const val DEFAULT_FORMAT = "yyyy-MM-dd"
        const val DEFAULT_VIEW_FORMAT = "dd MMM yyyy"
        const val DEFAULT_VIEW_TIME_FORMAT = "dd MMM yyyy, HH:mm"
        val DEFAULT_LOCALE = Locale("in", "ID")
        const val YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val FORMAT_TIME_DETAIL = "HH:mm"
        const val FORMAT_DATE_LOCAL_DETAIL = "EEEE, dd LLLL yyyy"
        const val FORMAT_DATE_SHORT_MONTH = "EEEE, dd MMM yyyy"
        const val YYYYMMDD = "yyyyMMdd"

        private const val DEFAULT_LAST_HOUR_IN_DAY = 23
        private const val DEFAULT_LAST_MIN_IN_DAY = 59
        private const val DEFAULT_LAST_SEC_IN_DAY = 59

        fun formatToUi(dateStr: String): String {
            return formatDate(DEFAULT_FORMAT, DEFAULT_VIEW_FORMAT, dateStr)
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
                    toFormat.format(date)
                } else {
                    dateString
                }
            } catch (e: ParseException) {
                e.printStackTrace()
                dateString
            }
        }

        fun stringToDate(input: String): Date {
            val fromFormat: DateFormat = SimpleDateFormat(DEFAULT_FORMAT, DEFAULT_LOCALE)
            return try {
                fromFormat.parse(input)
            } catch (e: ParseException) {
                e.printStackTrace()
                throw RuntimeException("Date does not valid ($input) with format$DEFAULT_FORMAT")
            }
        }

        @JvmStatic
        fun stringToDate(format: String, input: String): Date {
            val fromFormat: DateFormat = SimpleDateFormat(format, DEFAULT_LOCALE)
            return try {
                fromFormat.parse(input)
            } catch (e: ParseException) {
                e.printStackTrace()
                throw RuntimeException("Date does not valid ($input) with format $format")
            }
        }

        fun dateToString(currentDate: Date, outputFormat: String): String {
            val format: DateFormat = SimpleDateFormat(outputFormat, DEFAULT_LOCALE)
            return format.format(currentDate)
        }

        fun addDate(date: Date, days: Int): Date {
            val cal = GregorianCalendar()
            cal.time = date
            cal.add(Calendar.DATE, days)
            return cal.time
        }

        @JvmStatic
        fun removeTime(date: Date): Date {
            val cal = Calendar.getInstance()
            cal.time = date
            cal[Calendar.HOUR_OF_DAY] = 0
            cal[Calendar.MINUTE] = 0
            cal[Calendar.SECOND] = 0
            cal[Calendar.MILLISECOND] = 0
            return cal.time
        }

        val currentDate: Date
            get() {
                val now = currentCalendar
                return now.time
            }
        val currentCalendar: Calendar
            get() = Calendar.getInstance()

        fun trimDate(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar[Calendar.HOUR_OF_DAY] = DEFAULT_LAST_HOUR_IN_DAY
            calendar[Calendar.MINUTE] = DEFAULT_LAST_MIN_IN_DAY
            calendar[Calendar.SECOND] = DEFAULT_LAST_SEC_IN_DAY
            return calendar.time
        }

        fun addTimeToCurrentDate(field: Int, value: Int): Date {
            val now = currentCalendar
            now.add(field, value)
            return now.time
        }

        @JvmStatic
        fun addTimeToSpesificDate(date: Date, field: Int, value: Int): Date {
            val now = currentCalendar
            now.time = date
            now.add(field, value)
            return now.time
        }

        @JvmStatic
        fun countDayDifference(format: String, day1: String, day2: String): Long {
            return (stringToDate(format, day2).time - stringToDate(format, day1).time) / TimeUnit.DAYS.toMillis(1)
        }
    }
}