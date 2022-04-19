package com.tokopedia.tokopedianow.common.util

import android.text.format.DateFormat
import java.lang.Exception
import java.util.*

object DateUtil {

    private const val MIN_KEYWORD_CHARACTER_COUNT = 3

    fun getGregorianCalendar(date: String): GregorianCalendar {
        var returnDate = GregorianCalendar()
        val splitDefDate = date.split("-")
        if (splitDefDate.isNotEmpty() && splitDefDate.size == MIN_KEYWORD_CHARACTER_COUNT) {
            returnDate = stringToCalendar("${splitDefDate[0].toInt()}-${(splitDefDate[1].toInt()-1)}-${splitDefDate[2].toInt()}")
        }
        return returnDate
    }

    fun calendarToStringFormat(dateParam: Calendar, format: String) : CharSequence {
        return try {
            DateFormat.format(format, dateParam.time)
        } catch (e: Exception) {
            DateFormat.format(format, GregorianCalendar().time)
        }
    }

    private fun stringToCalendar(stringParam: CharSequence) : GregorianCalendar {
        val split = stringParam.split("-")
        return if (split.isNotEmpty() && split.size == MIN_KEYWORD_CHARACTER_COUNT) {
            GregorianCalendar(split[0].toInt(), split[1].toInt(), split[2].toInt())
        } else GregorianCalendar()
    }
}