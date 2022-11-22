package com.tokopedia.play_common.util.datetime

import com.tokopedia.utils.date.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By : Jonathan Darwin on August 24, 2021
 */
object PlayDateTimeFormatter {

    private const val COUNTRY_ID = "ID"
    private const val LANGUAGE_ID = "id"
    private const val GMT = "GMT"
    private const val Z = "Z"
    private const val GMT07 = "+0700"

    const val yyyyMMddTHHmmss = "yyyy-MM-dd'T'HH:mm:ss"
    const val ddMMMMyyyy_HHmm = "dd MMMM yyyy - HH:mm"
    const val ddMMMyyyy_HHmm = "dd MMM yyyy - HH:mm"
    const val ddMMMyyy_HHmmWIB = "dd MMM yyyy, HH:mm 'WIB'"
    const val dMMMMyyyy = "d MMMM yyyy"

    private const val GMT_DIVIDER = 100

    private val locale = Locale(LANGUAGE_ID, COUNTRY_ID)

    fun formatDate(
        raw: String,
        inputPattern: String = yyyyMMddTHHmmss,
        outputPattern: String = ddMMMyyyy_HHmm
    ): String {
        return try {
            val inputFormat = SimpleDateFormat(inputPattern, locale)
            val outputFormat = SimpleDateFormat(outputPattern, locale)
            val date = inputFormat.parse(raw)

            date?.let {
                val calendar = Calendar.getInstance()
                calendar.time = it

                val diff = (getDeviceGMT().toInt() - GMT07.toInt())
                if(diff != 0) {
                    calendar.add(Calendar.HOUR_OF_DAY, diff / GMT_DIVIDER)
                    calendar.add(Calendar.MINUTE, diff % GMT_DIVIDER)
                }

                return@let outputFormat.format(calendar.time)
            } ?: raw
        }
        catch (e: Exception){
            raw
        }
    }

    fun formatDate(
        date: Date,
        outputPattern: String = ddMMMyyyy_HHmm
    ): String? {
        return try {
            val outputFormat = SimpleDateFormat(outputPattern, locale)
            outputFormat.format(date)
        } catch (e: Exception) {
            null
        }
    }

    private fun getDeviceGMT(): String {
        return try {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone(GMT), locale)
            val dateFormat = SimpleDateFormat(Z, locale)
            dateFormat.format(calendar.time)
        }
        catch (e: Exception) {
            GMT07
        }
    }

    fun convertToCalendar(
        raw: String,
        pattern: String = yyyyMMddTHHmmss
    ): Calendar? {
        return try {
            val format = SimpleDateFormat(pattern, locale)
            val date = format.parse(raw)

            date?.let {
                val calendar = Calendar.getInstance()
                calendar.time = it

                val diff = (getDeviceGMT().toInt() - GMT07.toInt())
                if(diff != 0) {
                    calendar.add(Calendar.HOUR_OF_DAY, diff / GMT_DIVIDER)
                    calendar.add(Calendar.MINUTE, diff % GMT_DIVIDER)
                }

                return@let calendar
            }
        }
        catch (e: Exception) {
            null
        }
    }

    fun getTodayDateTime(pattern: String): String {
        return try {
            val outputFormatter = SimpleDateFormat(pattern, locale)
            return outputFormatter.format(Date())
        }
        catch (e: Exception) {
            ""
        }
    }

    fun getToday(): Date {
        return Calendar.getInstance(TimeZone.getTimeZone(GMT), locale).time
    }

    fun Date.getDayDiffFromToday(): Long {
        val leftDate = this.trimDate()
        val rightDate = getToday()
        val diff = leftDate.time - rightDate.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }
}