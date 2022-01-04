package com.tokopedia.campaignlist.common.util

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DateManager @Inject constructor() {

    companion object {
        const val DATE_TIME_FORMAT_DAY_MONTH_YEAR = "ddMMyy"
    }

    fun getCurrentDateTime(expectedDateTimeFormat: String): String {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat(expectedDateTimeFormat, Locale.US)
        return dateFormat.format(date)
    }

}