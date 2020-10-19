package com.tokopedia.travel_slice.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author by jessica on 19/10/20
 */

object TravelDateUtils {

    const val YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss"
    const val YYYY_MM_DD = "yyyy-MM-dd"
    private val DEFAULT_LOCALE = Locale("in", "ID")

    fun stringToDate(format: String, input: String): Date {
        val fromFormat: DateFormat = SimpleDateFormat(format, DEFAULT_LOCALE)
        return try {
            fromFormat.parse(input)
        } catch (e: ParseException) {
            e.printStackTrace()
            throw RuntimeException("Date doesnt valid ($input) with format$format")
        }
    }

    fun dateToString(format: String, input: Date): String {
        val formatDate: DateFormat = SimpleDateFormat(format, DEFAULT_LOCALE)
        return formatDate.format(input)
    }

    fun formatDate(currentFormat: String, newFormat: String, dateString: String): String {
        return formatDate(currentFormat, newFormat, dateString, DEFAULT_LOCALE)
    }

    private fun formatDate(currentFormat: String, newFormat: String, dateString: String, locale: Locale): String {
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

    fun addTimeToSpesificDate(date: Date, field: Int, value: Int): Date {
        val now: Calendar = Calendar.getInstance()
        now.time = date
        now.add(field, value)
        return now.time
    }

    fun getTodayDate(format: String): String {
        val now = Calendar.getInstance().time
        return dateToString(format, now)
    }
}