package com.tokopedia.shop.flashsale.presentation.list.quotamonitoring

import java.util.*

object DateHelper {
    fun getDayAfterTomorrowInUnix(): Long {
        val date = Calendar.getInstance()
        date.add(Calendar.DAY_OF_MONTH, 2)
        return date.time.toInstant().epochSecond
    }
}
