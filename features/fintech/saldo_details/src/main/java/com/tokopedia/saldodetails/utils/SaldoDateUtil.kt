package com.tokopedia.saldodetails.utils

import com.tokopedia.unifycomponents.Label
import com.tokopedia.utils.date.DateUtil
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object SaldoDateUtil {


    val DATE_PATTERN_FROM_SERVER = "yyyy-MM-dd HH:mm:ss"
    val DATE_PATTERN_FOR_UI = "dd MMM yyyy HH:mm"

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

    fun isDatesAreSame(date1 : Date?, date2 : Date?): Boolean {
        if(date1 == null || date2 == null)
            return false
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2
        return (cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR] &&
                cal1[Calendar.YEAR] == cal2[Calendar.YEAR])
    }

    fun setMidnight(cal: Calendar) {
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
    }


    fun getLocalLabelColor(serverColorInt: Int): Int {
        return when(serverColorInt){
            1-> Label.GENERAL_LIGHT_GREEN
            2-> Label.GENERAL_LIGHT_ORANGE
            3-> Label.GENERAL_LIGHT_RED
            else -> Label.GENERAL_DARK_GREY
        }
    }
}