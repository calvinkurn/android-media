package com.tokopedia.campaign.utils.extension

import com.tokopedia.campaign.utils.constant.LocaleConstant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


fun String.toDate(inputFormat: String, timeZone: TimeZone = TimeZone.getDefault()): Date {
    return try {
        val format = SimpleDateFormat(inputFormat, LocaleConstant.INDONESIA)
        format.timeZone = timeZone
        format.parse(this) ?: Date()
    } catch (e: Exception) {
        Date()
    }
}