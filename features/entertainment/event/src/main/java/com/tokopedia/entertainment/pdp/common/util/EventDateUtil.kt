package com.tokopedia.entertainment.pdp.common.util

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object EventDateUtil {


    fun stringToDate(format: String, input: String): Date {
        return try {
            val fromFormat: DateFormat = SimpleDateFormat(format, Locale("in", "ID"))
            fromFormat.parse(input)
        } catch (e: ParseException) {
            e.printStackTrace()
            throw RuntimeException("Date doesnt valid ($input) with format$format")
        }
    }

    fun getDateString(format: String,time: Int): String {
        val fromFormat: DateFormat = SimpleDateFormat(format, Locale("in", "ID"))
        return fromFormat.format(time * 1000L)
    }

    fun stringToDateRedeem(input: String): String{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val date = inputFormat.parse(input)
        val formattedDate = outputFormat.format(date)
        return formattedDate
    }

    fun convertUnixToToday(date: Long): Long{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date * 1000
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis / 1000
    }
}