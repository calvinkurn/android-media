package com.tokopedia.travelcalendar.view

import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Created by nabillasabbaha on 15/05/18.
 */
object DateCalendarUtil {

    val DEFAULT_LOCALE = Locale("in", "ID")

    val calendar: Calendar
        get() = Calendar.getInstance()

    fun getZeroTimeDate(fecha: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = fecha
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val res = calendar.time
        return res
    }
}
