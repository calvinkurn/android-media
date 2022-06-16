package com.tokopedia.tokomember_seller_dashboard.util

import android.annotation.SuppressLint
import com.tokopedia.utils.date.toDate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

object TmDateUtil {

    @SuppressLint("SimpleDateFormat")
    fun getTimeFromUnix(calendar: Calendar): String {
        return SimpleDateFormat("HH:mm").format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateFromUnix(calendar: Calendar): String {
        return SimpleDateFormat("yyyy-MM-dd").format(calendar.time)
    }

    fun setDate(time: String): String {
        val selectedTime = time.substringBefore(" ")
        val date = selectedTime.toDate("yyyy-MM-dd")
        val day = SimpleDateFormat("dd").format(date)
        val month = SimpleDateFormat("MMMM").format(date)
        val year = selectedTime.substringBefore("-")
        return "$day $month $year"
    }

    fun setTime(time: String): String {
        val selectedTime  = time.substringAfter(" ").substringBefore(" ").substringBeforeLast(":").substringAfter("T")
        return "$selectedTime WIB"
    }

    fun setTimeEnd(time: String): String {
        val parseTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale("id", "ID"))
        parseTime.timeZone = TimeZone.getTimeZone("UTC")
        val date = parseTime.parse(time + "00")
        val calendar = Calendar.getInstance(Locale("id", "ID"))
        date?.let {
            calendar.time = it
            calendar.add(Calendar.HOUR, -1)
        }
        val requireTime =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale("id", "ID")).format(calendar.time)
        return requireTime.substringAfter(" ").substringBefore(" ").substringBeforeLast(":")
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateTime(t: Date): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z" , Locale("id","ID")).format(t)
    }

    @SuppressLint("SimpleDateFormat")
    fun addDuration(time: String , duration: Int):String {
        //+00 to match Format Z , BE support up to 2 character of Z
        val parseTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z" , Locale("id","ID")).parse(time+"00")
        val calendar = Calendar.getInstance()
        parseTime?.let {
            calendar.time = it
            calendar.add(Calendar.MONTH , duration )
            calendar.add(Calendar.MINUTE,30)
        }
        var requireTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z" , Locale("id","ID")).format(calendar.time)
        requireTime = requireTime.substring(0, requireTime.length-2)
        return requireTime
    }

    fun getTimeDuration(startTime: String, endTime: String): Int {
        val parseStartTime = SimpleDateFormat("yyyy-MM-dd" , Locale("id","ID")).parse(startTime)
        val parseEndTime = SimpleDateFormat("yyyy-MM-dd" , Locale("id","ID")).parse(endTime)
        val cal = Calendar.getInstance()
        cal.setTime(parseStartTime)
        val startMonth = cal.get(Calendar.MONTH)
        cal.setTime(parseEndTime)
        val endMonth = cal.get(Calendar.MONTH)
        return endMonth.minus(startMonth).absoluteValue
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDuration(time: String):String{
        //+00 to match Format Z , BE support up to 2 character of Z
        val parseTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z" , Locale("id","ID")).parse(time+"00")
        val calendar = Calendar.getInstance()
        parseTime?.let {
            calendar.time = it
        }
        var requireTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z" , Locale("id","ID")).format(calendar.time)
        requireTime = requireTime.substring(0, requireTime.length-2)
        return requireTime
    }

    fun getTimeInMillis(dateStr: String?, pattern: String = "yyyy-MM-dd HH:mm:ss Z"): String {
        val parseTime = SimpleDateFormat(pattern , Locale("id","ID"))
        parseTime.timeZone = TimeZone.getTimeZone("UTC")
        val date = parseTime.parse(dateStr+"00")
        return try {
            (date?.time?.div(1000)).toString()
        } catch (e: Exception) {
            "0"
        }
    }


    fun getTimeInMillis(dateStr: String): String {
        val parseTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z" , Locale("id","ID"))
        parseTime.timeZone = TimeZone.getTimeZone("UTC")
        val date = parseTime.parse(dateStr+"00")
        return try {
            (date?.time?.div(1000)).toString()
        } catch (e: Exception) {
            "0"
        }
    }

    fun getTimeInMillisEnd(dateStr: String): String {
        val parseTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z" ,Locale("id","ID"))
        parseTime.timeZone = TimeZone.getTimeZone("UTC")
        val date = parseTime.parse(dateStr+"00")
        return try {
            val calendar = Calendar.getInstance(Locale("id","ID"))
            date?.let {
                calendar.time = it
                calendar.add(Calendar.HOUR,-1)
            }
            (calendar.timeInMillis/1000).toString()
        } catch (e: Exception) {
            "0"
        }
    }

    fun getDateForCouponProgram(dateStr: String): String {
        return dateStr.substringBefore(" ")
    }

    fun getTimeMillisForCouponValidate(dateInput: String): String{
        //replace z by +0700
        var dateStr = dateInput.replace("T", "")
        dateStr = dateStr.replace("Z", " +0700")

        val parseTime = SimpleDateFormat("yyyy-MM-ddHH:mm:ss Z" , Locale("id","ID"))
        parseTime.timeZone = TimeZone.getTimeZone("UTC")
        val date = parseTime.parse(dateStr)
        return (date?.time?.div(1000)).toString()
    }

    fun getTimeMillisForCouponValidateEnd(dateInput: String): String{
        //replace z by +0700
        var dateStr = dateInput.replace("T", "")
        dateStr = dateStr.replace("Z", " +0700")

        val parseTime = SimpleDateFormat("yyyy-MM-ddHH:mm:ss Z" , Locale("id","ID"))
        parseTime.timeZone = TimeZone.getTimeZone("UTC")
        val date = parseTime.parse(dateStr)
        (date?.time?.div(1000)).toString()

        val calendar = Calendar.getInstance(Locale("id","ID"))
        date?.let {
            calendar.time = it
            calendar.add(Calendar.HOUR,-1)
        }
        return (calendar.timeInMillis/1000).toString()
    }
}