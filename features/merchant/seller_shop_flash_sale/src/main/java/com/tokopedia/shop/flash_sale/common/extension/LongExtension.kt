package com.tokopedia.shop.flash_sale.common.extension

import com.tokopedia.shop.flash_sale.common.constant.DateConstant
import com.tokopedia.shop.flash_sale.common.constant.LocaleConstant
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*

private const val EPOCH_TO_MILLIS_MULTIPLIER = 1000

fun Long.toDate(): Date {
    return Date(this)
}

fun Long.epochToDate(): Date {
    return try {
        val originalDate = Date(this * EPOCH_TO_MILLIS_MULTIPLIER)
        val simpleDateFormat = SimpleDateFormat(DateConstant.DATE_TIME, LocaleConstant.INDONESIA)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
        val stringifyOriginalDate = simpleDateFormat.format(originalDate)

        val outputFormat = SimpleDateFormat(DateConstant.DATE_TIME, LocaleConstant.INDONESIA)
        outputFormat.timeZone = TimeZone.getTimeZone("GMT")
        val date = outputFormat.parse(stringifyOriginalDate)



        val adjusted = Calendar.getInstance()
        adjusted.time = date
        val hour = adjusted.get(Calendar.HOUR_OF_DAY)
        val decreased = hour - 7
        adjusted.set(Calendar.HOUR_OF_DAY, decreased)

        val time = adjusted.time
        return  time ?: Date() //Converted to local time (WIB)
    }catch (e:Exception) {
        Date(this * EPOCH_TO_MILLIS_MULTIPLIER)
    }
}

fun Long.epochTo(desiredOutputFormat: String): String {
    val dateFormat = SimpleDateFormat(desiredOutputFormat, LocaleConstant.INDONESIA)
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    val date = Date(this * EPOCH_TO_MILLIS_MULTIPLIER)

    return dateFormat.format(date)
}