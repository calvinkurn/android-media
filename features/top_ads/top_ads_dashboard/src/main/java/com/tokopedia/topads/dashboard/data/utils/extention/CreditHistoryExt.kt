package com.tokopedia.topads.dashboard.data.utils.extention

import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.dashboard.data.model.credit_history.CreditHistory
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

val CreditHistory.formatedDate: String
    get() {
        val formatter = SimpleDateFormat(TopAdsCommonConstant.ISO8601_DATE_FORMAT, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            val date = formatter.parse(this.date)

            val dateFormat = SimpleDateFormat(if (showTimestamp) FORMAT_DATE_WITH_TIMESTAMP else FORMAT_DATE_NO_TIMESTAMP,
                    Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            dateFormat.format(date)

        } catch (e: Exception){
            ""
        }
    }


const val FORMAT_DATE_NO_TIMESTAMP = "dd MMMM yyyy"
const val FORMAT_DATE_WITH_TIMESTAMP = "$FORMAT_DATE_NO_TIMESTAMP, HH:mm"