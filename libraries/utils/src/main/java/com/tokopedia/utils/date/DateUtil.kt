package com.tokopedia.utils.date

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author by furqan on 24/06/2021
 *
 * This Util will help to format object related to Date,
 * such as Date to String, String to Date, format Date, etc
 *
 */
object DateUtil {

    const val LAST_HOUR_IN_A_DAY = 23
    const val LAST_MIN_IN_AN_HOUR = 59
    const val LAST_SEC_IN_A_MIN = 59

    val DEFAULT_LOCALE = Locale("in", "ID")
    val DEFAULT_TIMEZONE: TimeZone = TimeZone.getTimeZone("GMT+7")

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

    /**
     * Function to get the day difference from date 2 - date 1
     *
     * @param date1 date string
     * @param date2 date string
     *
     * @return Long object of day difference between two dates
     */
    fun getDayDiff(date1: String, date2: String): Long {
        val leftDate = date2.toDate(YYYY_MM_DD).trimDate()
        val rightDate = date1.toDate(YYYY_MM_DD).trimDate()
        val diff = leftDate.time - rightDate.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }

    /**
     * Function to get the day difference from input date and today
     *
     * @param dateStr date in string object
     *
     * @return Long object of day difference between two dates
     */
    fun getDayDiffFromToday(dateStr: String): Long {
        val date = dateStr.toDate(YYYY_MM_DD).trimDate()
        return date.getDayDiffFromToday()
    }

    /**
     * Function to get current calendar
     *
     * @return current Calendar Instance
     */
    fun getCurrentCalendar(): Calendar = Calendar.getInstance()

    /**
     * Function to get current date
     *
     * @return current Date
     */
    fun getCurrentDate(): Date = getCurrentCalendar().time

    /**
     * Function to format date based on user timezone
     *
     * @param currentFormat current format of date string
     * @param newFormat expected format for date string
     * @param dateString date in string
     *
     * @return String with new format
     */
    @JvmStatic
    fun formatDateByUsersTimezone(currentFormat: String,
                                  newFormat: String,
                                  dateString: String): String {
        val timeZone = TimeZone.getDefault()
        return formatDate(currentFormat,
                newFormat,
                dateString,
                DEFAULT_TIMEZONE,
                timeZone,
                DEFAULT_LOCALE)
    }

    /**
     * Function to format date
     *
     * @param currentFormat current format of date string
     * @param newFormat expected format for date string
     * @param dateString date in string
     * @param locale User Locale, default : ID Indonesia
     *
     * @return String with new format
     */
    @JvmOverloads
    fun formatDate(currentFormat: String,
                   newFormat: String,
                   dateString: String,
                   locale: Locale = DEFAULT_LOCALE): String {
        return try {
            val fromFormat: DateFormat = SimpleDateFormat(currentFormat, locale)
            fromFormat.isLenient = false
            val toFormat: DateFormat = SimpleDateFormat(newFormat, locale)
            toFormat.isLenient = false

            val date = fromFormat.parse(dateString)
            toFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            dateString
        }
    }

    /**
     * Function to format date from one timezone to another timezone
     *
     * @param currentFormat current format of date string
     * @param newFormat expected format for date string
     * @param dateString date in string
     * @param locale User Locale, default : ID Indonesia
     * @param fromTimeZone the date previous timezone
     * @param toTimezone the expected timezone for new date
     *
     * @return String with new format
     */
    fun formatDate(currentFormat: String,
                   newFormat: String,
                   dateString: String,
                   fromTimeZone: TimeZone,
                   toTimezone: TimeZone,
                   locale: Locale = DEFAULT_LOCALE): String {
        return try {
            val fromFormat: DateFormat = SimpleDateFormat(currentFormat, locale)
            fromFormat.timeZone = fromTimeZone
            fromFormat.isLenient = false
            val toFormat: DateFormat = SimpleDateFormat(newFormat, locale)
            toFormat.isLenient = false
            toFormat.timeZone = toTimezone

            val date = fromFormat.parse(dateString)
            toFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            dateString
        }
    }

    /**
     * Function to format date string to UI format
     * this function will format the date string
     * from yyyy-MM-dd format to dd MMM yyyy format
     *
     * @param dateStr date in string
     *
     * @return String with new format
     */
    @JvmStatic
    fun formatToUi(dateStr: String): String {
        return formatDate(YYYY_MM_DD, DEFAULT_VIEW_FORMAT, dateStr)
    }

}

/**
 * Function to format string to date
 *
 * @param format string input format eg. "yyyy-MM-dd", there are some available format above
 *
 * @return Date object from String input
 */
fun String?.toDate(format: String): Date {
    this?.let {
        val fromFormat: DateFormat = SimpleDateFormat(format, DateUtil.DEFAULT_LOCALE)
        return try {
            fromFormat.parse(it) ?: throw ParseException("Failed to parse", 0)
        } catch (e: ParseException) {
            e.printStackTrace()
            throw RuntimeException("Date doesn't valid ($this) with format $format")
        }
    }
    throw RuntimeException("Date doesn't valid ($this) with format $format")
}

/**
 * Function to format date to string
 *
 * @param format expected result string format eg. "yyyy-MM-dd", there are some available format above
 *
 * @return String object from Date input with selected format
 */
fun Date.toString(format: String): String {
    val formatDate: DateFormat = SimpleDateFormat(format, DateUtil.DEFAULT_LOCALE)
    return formatDate.format(this)
}

/**
 * Function to trim time in the date to be the last time of the day
 * eg : 11-11-2021 23:59:59
 *
 * @return String with trimmed time
 */
fun Date.trimDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar[Calendar.HOUR_OF_DAY] = DateUtil.LAST_HOUR_IN_A_DAY
    calendar[Calendar.MINUTE] = DateUtil.LAST_MIN_IN_AN_HOUR
    calendar[Calendar.SECOND] = DateUtil.LAST_SEC_IN_A_MIN
    calendar[Calendar.MILLISECOND] = 0
    return calendar.time
}

/**
 * Function to remove time from date, so hour, minute, second and ms will be 0
 *
 * @return Date object with value 0 for hour, minute, second and ms
 */
fun Date.removeTime(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal[Calendar.HOUR_OF_DAY] = 0
    cal[Calendar.MINUTE] = 0
    cal[Calendar.SECOND] = 0
    cal[Calendar.MILLISECOND] = 0
    return cal.time
}

/**
 * Function to add some value for specific field in Date
 *
 * @param field field that want to be changed from date, eg. Calendar.MINUTE, Calendar.YEAR, etc
 * @param value additional value for the selected field
 *
 * @return Date object
 */
fun Date.addTimeToSpesificDate(field: Int, value: Int): Date {
    val now = DateUtil.getCurrentCalendar()
    now.time = this
    now.add(field, value)
    return now.time
}

/**
 * Function to get the day difference from input date and today
 *
 * @return Long object of day difference between two dates
 */
fun Date.getDayDiffFromToday(): Long {
    val leftDate = this.trimDate()
    val rightDate = DateUtil.getCurrentCalendar().time.trimDate()
    val diff = leftDate.time - rightDate.time
    return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
}