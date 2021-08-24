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

    fun formatDate(
        raw: String,
        inputFormat: String = yyyymmddThhmmss,
        outputFormat: String = ddmmmmyyyy_hhmm
    ): String {
        return try {
            val locale = Locale(LANGUAGE_ID, COUNTRY_ID)
            val input = SimpleDateFormat(inputFormat, locale)
            val output = SimpleDateFormat(outputFormat, locale)

            val date = input.parse(raw)
            date?.let {
                return@let output.format(date)
            } ?: raw
        }
        catch (e: Exception){
            raw
        }
    }
}