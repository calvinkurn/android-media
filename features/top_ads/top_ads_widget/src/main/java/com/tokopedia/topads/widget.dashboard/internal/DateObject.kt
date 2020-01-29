package com.tokopedia.topads.widget.dashboard.internal

import java.text.SimpleDateFormat
import java.util.*

/**
 * Author errysuprayogi on 25,October,2019
 */
object DateObject {
    private val REQUEST_DATE_FORMAT = "yyyy-MM-dd"

    private fun getDate(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return calendar.time
    }

    val endDate = SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).format(getDate(0))
    val startDate = SimpleDateFormat(REQUEST_DATE_FORMAT, Locale.ENGLISH).format(getDate(7))

}
