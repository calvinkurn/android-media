package com.tokopedia.tokomember_seller_dashboard.util

import android.annotation.SuppressLint
import com.tokopedia.utils.date.toDate
import java.text.SimpleDateFormat
import java.util.*

object TmDateUtil {

    @SuppressLint("SimpleDateFormat")
    fun getTimeFromUnix(calendar: Calendar): String {
        return SimpleDateFormat("HH:mm").format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateFromUnix(calendar: Calendar): String {
        return SimpleDateFormat("yyyy-mm-dd").format(calendar.time)
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

    @SuppressLint("SimpleDateFormat")
    fun convertDateTime(t: Date): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").format(t)
    }

    @SuppressLint("SimpleDateFormat")
    fun addDuration(time: String , duration: Int):String {
        //+00 to match Format Z , BE support up to 2 character of Z
        val parseTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(time+"00")
        val calendar = Calendar.getInstance()
        parseTime?.let {
            calendar.time = it
            calendar.add(Calendar.MONTH , duration )
        }
        var requireTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").format(calendar.time)
        requireTime = requireTime.substring(0, time.length-2)
        return requireTime
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDuration(time: String):String{
        //+00 to match Format Z , BE support up to 2 character of Z
        val parseTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(time+"00")
        val calendar = Calendar.getInstance()
        parseTime?.let {
            calendar.time = it
        }
        var requireTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").format(calendar.time)
        requireTime = requireTime.substring(0, time.length-2)
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
}