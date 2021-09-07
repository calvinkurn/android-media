package com.tokopedia.play_common.util.datetime

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By : Jonathan Darwin on September 03, 2021
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

    private const val GMT_DIVIDER = 100

    private val locale = Locale(LANGUAGE_ID, COUNTRY_ID)

    fun formatDate(
        raw: String,
        inputPattern: String = yyyyMMddTHHmmss,
        outputPattern: String = ddMMMMyyyy_HHmm
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

    fun getDiffDayHourMinute(
        rawDate: String,
        pattern: String = ddMMMMyyyy_HHmm // TODO("Should be changed to yyyyMMddTHHmmss")
    ): Triple<Long, Long, Long> {
        return try {
            val todayCalendar = Calendar.getInstance()
            val targetCalendar = convertToCalendar(rawDate, pattern)

            targetCalendar?.let {
                var diff = it.timeInMillis - todayCalendar.timeInMillis
                if(diff < 0) diff *= -1

                val days = diff / (24 * 60 * 60 * 1000)
                diff %= (24 * 60 * 60 * 1000)

                val hours = diff / (60 * 60 * 1000)
                diff %= (60 * 60 * 1000)

                val minutes = diff / (60 * 1000)
                diff %= (60 * 1000)

                Triple(days, hours, minutes)
            } ?: kotlin.run {
                Triple(-1, -1, -1)
            }
        }
        catch (e: Exception) {
            Triple(-1, -1, -1)
        }
    }

    fun convertToCalendar(
        rawDate: String,
        pattern: String = ddMMMMyyyy_HHmm // TODO("Should be changed to yyyyMMddTHHmmss")
    ) : Calendar? {
        return try {
            val format = SimpleDateFormat(pattern, locale)
            val date = format.parse(rawDate)

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
}