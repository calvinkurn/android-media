package com.tokopedia.topads.common.data.util

import android.content.Context
import com.tokopedia.datepicker.LocaleUtils
import java.util.*

object DateTimeUtils {

    fun Context.getToday() = GregorianCalendar(LocaleUtils.getCurrentLocale(this))

    fun Context.getSpecifiedDateFromToday(hours: Int = 0, days: Int = 0, month: Int = 0, years: Int = 0) =
            getToday().apply {
                add(Calendar.HOUR, hours)
                add(Calendar.DATE, days)
                add(Calendar.MONTH, month)
                add(Calendar.YEAR, years)
            }

    fun getSpecifiedDateFromStartDate(startCalendar: GregorianCalendar?, hours: Int = 0, days: Int = 0, month: Int = 0, years: Int = 0): GregorianCalendar? =
            startCalendar?.let { startDate ->
                GregorianCalendar().apply {
                    time = startDate.time
                    add(Calendar.HOUR, hours)
                    add(Calendar.DATE, days)
                    add(Calendar.MONTH, month)
                    add(Calendar.YEAR, years)
                }
            }

}
