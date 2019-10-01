package com.tokopedia.travelcalendar

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by nabillasabbaha on 30/09/19.
 */

const val TRAVEL_CAL_YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'"
const val TRAVEL_CAL_YYYY_MM_DD = "yyyy-MM-dd"
const val TRAVEL_CAL_DEFAULT_VIEW_FORMAT = "dd MMM yyyy"
const val TRAVEL_CAL_VIEW_FORMAT_WITHOUT_YEAR = "dd MMM"
const val TRAVEL_CAL_YYYYMMDD = "yyyyMMdd"
const val TRAVEL_CAL_YYYY = "yyyy"
const val TRAVEL_CAL_MM = "MM"
const val TRAVEL_CAL_M = "M"

private val DEFAULT_LOCALE = Locale("in", "ID")

fun String.stringToDate(format: String) : Date {
    val fromFormat = SimpleDateFormat(format, DEFAULT_LOCALE)
    try {
        return fromFormat.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        throw RuntimeException("Date doesnt valid ($this) with format$format")
    }
}

fun Date.dateToString(format: String): String {
    val formatDate = SimpleDateFormat(format, DEFAULT_LOCALE)
    return formatDate.format(this)
}