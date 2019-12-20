package com.tokopedia.salam.umrah.common.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author by M on 7/11/2019
 */

object UmrahDateUtil {
    const val DATE_WITH_YEAR_FORMAT = "dd MMM yyyy"
    const val DATE_WITH_YEAR_FULL_MONTH_FORMAT = "dd MMMM yyyy"
    const val DATE_WITHOUT_YEAR_FORMAT = "dd MMM"
    const val DATE_WITHOUT_YEAR_FULL_MONTH_FORMAT = "dd MMMM"
    const val DATE_ONLY_DATE_FORMAT = "dd"
    private const val YEAR_ONLY_DATE_FORMAT = "yyyy"
    private const val YYYY_MM_DD = "yyyy-MM-dd"
    const val DAY = "EEEE"

    fun getTime(format: String, date: String): String {
        return date.toDate(YYYY_MM_DD).toString(format)
    }

    fun getDateGregorianID(date: Array<Int>, format: String): String {
        val dateRes = "${date[2]}-${date[1] + 1}-${date[0]}"
        return getTime(format, dateRes)
    }

    fun getDateGregorian(date: Array<Int>): String {
        return "${date[2]}-${String.format("%02d", date[1] + 1)}-${String.format("%02d", date[0])}"
    }

    fun getYearNow(): String {
        val date = getCurrentDateTime()
        return date.toString(YEAR_ONLY_DATE_FORMAT)
    }

    fun Date.toString(format: String): String {
        val formatter = SimpleDateFormat(format, Locale("id"))
        return formatter.format(this)
    }

    private fun String.toDate(format: String): Date {
        val fromFormat = SimpleDateFormat(format, Locale("id"))
        return try {
            fromFormat.parse(this)
        } catch (var4: ParseException) {
            var4.printStackTrace()
            throw RuntimeException("Date doesn't valid ($this) with format$format")
        }
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}