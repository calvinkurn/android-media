package com.tokopedia.play.widget.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By : Jonathan Darwin on August 24, 2021
 */
object PlayWidgetDateFormatter {

    private const val COUNTRY_ID = "ID"
    private const val LANGUAGE_ID = "id"
    private const val yyyymmddThhmmss = "yyyy-MM-dd'T'HH:mm:ss"
    private const val ddmmmmyyyy_hhmm = "dd MMMM yyyy - HH:mm"
    private const val GMT = "GMT"
    private const val Z = "Z"
    private const val GMT07 = "+0700"

    private val locale = Locale(LANGUAGE_ID, COUNTRY_ID)

    fun formatDate(
        raw: String,
        inputFormat: String = yyyymmddThhmmss,
        outputFormat: String = ddmmmmyyyy_hhmm
    ): String {
        getDeviceGMT()
        return try {
            val input = SimpleDateFormat(inputFormat, locale)
            val output = SimpleDateFormat(outputFormat, locale)
            val date = input.parse(raw)

            date?.let {
                val diff = (getDeviceGMT().toInt() - GMT07.toInt()) / 100
                val time = it.time + (diff * 60 * 60 * 1000)

                return@let output.format(Date(time))
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
}