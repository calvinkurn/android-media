package com.tokopedia.saldodetails.utils

import com.tokopedia.utils.date.DateUtil
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object SaldoDateUtil {

    fun formatDate(currentFormat: String,
                   newFormat: String,
                   dateString: String,
                   currentLocale: Locale = DateUtil.DEFAULT_LOCALE,
                   locale: Locale = DateUtil.DEFAULT_LOCALE
    ): String {
        return try {
            val fromFormat: DateFormat = SimpleDateFormat(currentFormat, currentLocale)
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
    fun getInitialDateRange(onInitialDateRangeCreated : (startDate : Date, endDate : Date) -> Unit){
        val endCalender = GregorianCalendar.getInstance()
        val startCalender = GregorianCalendar().apply {
            time = endCalender.time
            add(Calendar.DAY_OF_MONTH, -1)
        }
        setMidnight(startCalender)
        setMidnight(endCalender)
        onInitialDateRangeCreated(startCalender.time, endCalender.time)
    }

    fun setMidnight(cal: Calendar) {
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
    }
}