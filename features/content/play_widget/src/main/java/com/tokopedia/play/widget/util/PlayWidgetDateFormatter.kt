package com.tokopedia.play.widget.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created By : Jonathan Darwin on August 24, 2021
 */
object PlayWidgetDateFormatter {

    private const val COUNTRY_ID = "ID"
    private const val LANGUAGE_ID = "id"
    private const val GMT = "GMT"
    private const val Z = "Z"
    private const val GMT07 = "+0700"

    private const val yyyyMMddTHHmmss = "yyyy-MM-dd'T'HH:mm:ss"
    private const val ddMMMMyyyy_HHmm = "dd MMMM yyyy - HH:mm"

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
                    calendar.add(Calendar.HOUR_OF_DAY, diff / 100)
                    calendar.add(Calendar.MINUTE, diff % 100)
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
}