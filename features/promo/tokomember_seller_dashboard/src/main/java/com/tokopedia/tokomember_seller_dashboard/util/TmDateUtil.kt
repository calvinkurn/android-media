package com.tokopedia.tokomember_seller_dashboard.util

import android.annotation.SuppressLint
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.utils.date.toDate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

const val DATE_FORMAT = "yyyy-MM-dd"
const val HOUR_MIN_FORMAT = "HH:mm"
const val DD_FORMAT = "dd"
const val SIMPLE_DATE_FORMAT_Z = "yyyy-MM-dd HH:mm:ss Z"
const val ISO_8601_UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
const val SIMPLE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
const val UTC = "UTC"
val locale = Locale("in", "ID")

object TmDateUtil {

    @SuppressLint("SimpleDateFormat")
    fun getTimeFromUnix(calendar: Calendar): String {
        return SimpleDateFormat(HOUR_MIN_FORMAT).format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateFromUnix(calendar: Calendar): String {
        return SimpleDateFormat(DATE_FORMAT).format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun setDatePreview(time: String, sdf: String = SIMPLE_DATE_FORMAT): String {
        val startDate = GregorianCalendar(locale)
        val sds = SimpleDateFormat(sdf, locale)
        var time = time
        if(sdf != DATE_FORMAT){
            time += "00"
        }
        startDate.time = sds.parse(time) ?: Date()
//        val dayStart = startDate.get(Calendar.DAY_OF_WEEK)
//        val dayOfWeekStart = getDayOfWeekID(dayStart)

        val month = startDate.getDisplayName(Calendar.MONTH, Calendar.LONG, LocaleUtils.getIDLocale())?.substring(0, 3)

        val selectedTime = time.substringBefore(" ")
        val date = selectedTime.toDate(DATE_FORMAT)
        val day = SimpleDateFormat(DD_FORMAT).format(date)
        val year = selectedTime.substringBefore("-")
        return "$day $month $year"
    }

    fun setTime(time: String): String {
        val selectedTime = time.substringAfter(" ").substringBefore(" ").substringBeforeLast(":")
            .substringAfter("T")
        return "$selectedTime WIB"
    }

    fun setTimeStart(time: String): String {
        val parseTime = SimpleDateFormat(SIMPLE_DATE_FORMAT_Z, locale)
        parseTime.timeZone = TimeZone.getTimeZone(UTC)
        val date = parseTime.parse(time + "00")
        val calendar = Calendar.getInstance(locale)
        date?.let {
            calendar.time = it
        }
        val requireTime =
            SimpleDateFormat(SIMPLE_DATE_FORMAT_Z, locale).format(calendar.time)

        return requireTime.substringAfter(" ").substringBefore(" ").substringBeforeLast(":")
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateTime(t: Date): String {
        return SimpleDateFormat(SIMPLE_DATE_FORMAT_Z, locale).format(t)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateTimeRemoveTimeDiff(t: Date): String {
        return SimpleDateFormat(SIMPLE_DATE_FORMAT_Z, locale).format(t).run {
            this.substring(0, this.length - 2)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun addDuration(time: String, duration: Int): String {
        //+00 to match Format Z , BE support up to 2 character of Z
        val parseTime = SimpleDateFormat(SIMPLE_DATE_FORMAT_Z, locale).parse(time + "00")
        val calendar = Calendar.getInstance()
        parseTime?.let {
            calendar.time = it
            calendar.add(Calendar.MONTH, duration)
        }
        var requireTime = SimpleDateFormat(SIMPLE_DATE_FORMAT_Z, locale).format(calendar.time)
        requireTime = requireTime.substring(0, requireTime.length - 2)
        return requireTime
    }

    fun getTimeDuration(startTime: String, endTime: String): Int {
        val parseStartTime = SimpleDateFormat(DATE_FORMAT, locale).parse(startTime)
        val parseEndTime = SimpleDateFormat(DATE_FORMAT, locale).parse(endTime)
        val cal = Calendar.getInstance()
        cal.time = parseStartTime
        val startMonth = cal.get(Calendar.MONTH)
        cal.time = parseEndTime
        val endMonth = cal.get(Calendar.MONTH)
        return endMonth.minus(startMonth).absoluteValue
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDuration(time: String): String {
        //+00 to match Format Z , BE support up to 2 character of Z
        val parseTime = SimpleDateFormat(SIMPLE_DATE_FORMAT_Z, locale).parse(time + "00")
        val calendar = Calendar.getInstance()
        parseTime?.let {
            calendar.time = it
        }
        var requireTime = SimpleDateFormat(SIMPLE_DATE_FORMAT_Z, locale).format(calendar.time)
        requireTime = requireTime.substring(0, requireTime.length - 2)
        return requireTime
    }

    fun getTimeInMillis(dateStr: String?, pattern: String = SIMPLE_DATE_FORMAT_Z): String {
        val parseTime = SimpleDateFormat(pattern, locale)
        parseTime.timeZone = TimeZone.getTimeZone(UTC)
        val date = parseTime.parse(dateStr + "00")
        return try {
            (date?.time?.div(1000)).toString()
        } catch (e: Exception) {
            "0"
        }
    }


    fun getTimeMillisForCouponValidate(dateInput: String): String {
        //replace z by +0700
        var dateStr = dateInput.replace("T", "")
        dateStr = dateStr.replace("Z", " +0700")

        val parseTime = SimpleDateFormat(SIMPLE_DATE_FORMAT , locale)
        parseTime.timeZone = TimeZone.getTimeZone(UTC)
        val date = parseTime.parse(dateStr)
        return (date?.time?.div(1000)).toString()
    }

    fun getTimeMillisForCouponValidateEnd(dateInput: String): String {
        //replace z by +0700
        var dateStr = dateInput.replace("T", "")
        dateStr = dateStr.replace("Z", " +0700")

        val parseTime = SimpleDateFormat(SIMPLE_DATE_FORMAT , locale)
        parseTime.timeZone = TimeZone.getTimeZone(UTC)
        val date = parseTime.parse(dateStr)
        (date?.time?.div(1000)).toString()

        val calendar = Calendar.getInstance(locale)
        date?.let {
            calendar.time = it
        }
        return (calendar.timeInMillis / 1000).toString()
    }

    fun getDayOfWeekID(day:Int) :String{
        return when(day){
            1 -> SUNDAY
            2 -> MONDAY
            3 -> TUESDAY
            4 -> WEDNESDAY
            5 -> THURSDAY
            6 -> FRIDAY
            7 -> SATURDAY
            else -> ""
        }
    }

    fun getDayFromTimeWindow(time: String): String {
        var time = time.substringBefore(" ")
        time += " 00:00:00"
        // setting time to 00 because 23:59 case will return next day
        val endDate = GregorianCalendar(locale)
        val sdf = SimpleDateFormat(DATE_FORMAT, locale)
        endDate.time = sdf.parse(time) ?: Date()
        return getDayOfWeekID(endDate.get(Calendar.DAY_OF_WEEK))
    }

    fun setTimeStartSingle(startTime: String?): String? {
        val parseTime = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
        parseTime.timeZone = TimeZone.getTimeZone(UTC)
        val date = parseTime.parse(startTime + "00")
        return try {
            val calendar = Calendar.getInstance(locale)
            date?.let {
                calendar.time = it
                calendar.add(Calendar.HOUR, 4)
            }
            parseTime.format(calendar.time)

        } catch (e: Exception) {
            "0"
        }
    }


    fun setDateFromDetails(time: String): String {
        // 2022-09-11T19:00:00Z00 >> Input
        // Min, 11 Sep 2022 >> Output
        var time = time.replace("T", " ")
        time = time.replaceAfter("Z", "")
        val day = getDayFromTimeWindow(time.substringBefore(" "))
        return "$day, ${setDatePreview(time)}"
    }

    fun setTimeFromDetails(time: String): String {
        // 2022-09-11T19:00:00Z00 >> Input
        // 19:00 WIB >> Output
        var time = time.replace("T", " ")
        time = time.replaceAfter("Z", "")
        return setTime(time)
    }

    fun getCalendarFromDetailsTime(time: String): Calendar{
        // 2022-09-11T19:00:00Z00 >> Input
        // Calendar >> Output

        var time = time.replace("T", " ")
        time = time.replaceAfter("Z", "")

        val parseTime = SimpleDateFormat(SIMPLE_DATE_FORMAT , locale)
        parseTime.timeZone = TimeZone.getTimeZone(UTC)
        val date = parseTime.parse(time)
        val calendar = Calendar.getInstance(locale)
        date?.let {
            calendar.time = it
        }
        return calendar
    }


    fun getDateFromISO(time:String?) : Date?{
        if(time.isNullOrEmpty() || !time.contains("T")) return null
        val sdf = SimpleDateFormat(ISO_8601_UTC_DATE_FORMAT, locale)
        return sdf.parse(time)
    }

}
