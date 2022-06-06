package com.tokopedia.shop.flash_sale.common.extension

import com.tokopedia.utils.date.DateUtil
import java.text.SimpleDateFormat
import java.util.*

fun String.digitsOnly(): Long {
    return try {
        this.filter { it.isDigit() }.toLong()
    } catch (e: Exception) {
        0
    }
}

fun String.toDate(inputFormat: String): Date {
    return try {
        val format = SimpleDateFormat(inputFormat, DateUtil.DEFAULT_LOCALE)
        format.timeZone = TimeZone.getTimeZone("GMT")
        format.parse(this) ?: Date()
    } catch (e: Exception) {
        e.printStackTrace()
        Date()
    }
}