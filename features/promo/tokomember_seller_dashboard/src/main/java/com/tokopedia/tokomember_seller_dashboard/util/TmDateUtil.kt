package com.tokopedia.tokomember_seller_dashboard.util

import android.annotation.SuppressLint
import com.tokopedia.utils.date.toDate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

const val DATE_FORMAT = "yyyy-MM-dd"
const val HOUR_MIN_FORMAT = "HH:mm"
const val DD_FORMAT = "dd"
const val MMMM_FORMAT = "MMMM"
const val SIMPLE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
const val UTC = "UTC"
val locale = Locale("id", "ID")

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
    fun setDate(time: String): String {
        val selectedTime = time.substringBefore(" ")
        val date = selectedTime.toDate(DATE_FORMAT)
        val day = SimpleDateFormat(DD_FORMAT).format(date)
        val month = SimpleDateFormat(MMMM_FORMAT).format(date)
        val year = selectedTime.substringBefore("-")
        return "$day $month $year"
    }

    fun setTime(time: String): String {
        val selectedTime = time.substringAfter(" ").substringBefore(" ").substringBeforeLast(":")
            .substringAfter("T")
        return "$selectedTime WIB"
    }

    fun setTimeEnd(time: String): String {
        val parseTime = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale)
        parseTime.timeZone = TimeZone.getTimeZone(UTC)
        val date = parseTime.parse(time + "00")
        val calendar = Calendar.getInstance(locale)
        date?.let {
            calendar.time = it
            calendar.add(Calendar.HOUR, -1)
        }
        val requireTime =
            SimpleDateFormat(SIMPLE_DATE_FORMAT, locale).format(calendar.time)

        return requireTime.substringAfter(" ").substringBefore(" ").substringBeforeLast(":")
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateTime(t: Date): String {
        return SimpleDateFormat(SIMPLE_DATE_FORMAT, locale).format(t)
    }

    @SuppressLint("SimpleDateFormat")
    fun addDuration(time: String, duration: Int): String {
        //+00 to match Format Z , BE support up to 2 character of Z
        val parseTime = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale).parse(time + "00")
        val calendar = Calendar.getInstance()
        parseTime?.let {
            calendar.time = it
            calendar.add(Calendar.MONTH, duration)
        }
        var requireTime = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale).format(calendar.time)
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
        val parseTime = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale).parse(time + "00")
        val calendar = Calendar.getInstance()
        parseTime?.let {
            calendar.time = it
        }
        var requireTime = SimpleDateFormat(SIMPLE_DATE_FORMAT, locale).format(calendar.time)
        requireTime = requireTime.substring(0, requireTime.length - 2)
        return requireTime
    }

    fun getTimeInMillis(dateStr: String?, pattern: String = SIMPLE_DATE_FORMAT): String {
        val parseTime = SimpleDateFormat(pattern, locale)
        parseTime.timeZone = TimeZone.getTimeZone(UTC)
        val date = parseTime.parse(dateStr + "00")
        return try {
            (date?.time?.div(1000)).toString()
        } catch (e: Exception) {
            "0"
        }
    }


    fun getTimeInMillis(dateStr: String): String {
        val parseTime = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale("id", "ID"))
        parseTime.timeZone = TimeZone.getTimeZone("UTC")
        val date = parseTime.parse(dateStr + "00")
        return try {
            (date?.time?.div(1000)).toString()
        } catch (e: Exception) {
            "0"
        }
    }

    fun getTimeInMillisEnd(dateStr: String): String {
        val parseTime = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale("id", "ID"))
        parseTime.timeZone = TimeZone.getTimeZone("UTC")
        val date = parseTime.parse(dateStr + "00")
        return try {
            val calendar = Calendar.getInstance(Locale("id", "ID"))
            date?.let {
                calendar.time = it
                calendar.add(Calendar.HOUR, -1)
            }
            (calendar.timeInMillis / 1000).toString()
        } catch (e: Exception) {
            "0"
        }
    }

    fun getDateForCouponProgram(dateStr: String): String {
        return dateStr.substringBefore(" ")
    }

    fun getTimeMillisForCouponValidate(dateInput: String): String {
        //replace z by +0700
        var dateStr = dateInput.replace("T", "")
        dateStr = dateStr.replace("Z", " +0700")

        val parseTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss" , Locale("id","ID"))
        parseTime.timeZone = TimeZone.getTimeZone("UTC")
        val date = parseTime.parse(dateStr)
        return (date?.time?.div(1000)).toString()
    }

    fun getTimeMillisForCouponValidateEnd(dateInput: String): String {
        //replace z by +0700
        var dateStr = dateInput.replace("T", "")
        dateStr = dateStr.replace("Z", " +0700")

        val parseTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss" , Locale("id","ID"))
        parseTime.timeZone = TimeZone.getTimeZone("UTC")
        val date = parseTime.parse(dateStr)
        (date?.time?.div(1000)).toString()

        val calendar = Calendar.getInstance(Locale("id", "ID"))
        date?.let {
            calendar.time = it
            calendar.add(Calendar.HOUR, -1)
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
}