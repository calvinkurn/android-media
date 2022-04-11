package com.tokopedia.shopdiscount.utils.extension

import com.tokopedia.utils.date.DateUtil
import java.text.SimpleDateFormat
import java.util.*

fun String.digitsOnly() : Int {
    return try {
        this.filter { it.isDigit() }.toInt()
    } catch (e: Exception) {
        0
    }
}

fun String.toDate(inputFormat : String) : Date{
    return try {
        val format = SimpleDateFormat(inputFormat, DateUtil.DEFAULT_LOCALE)
        format.parse(this) ?: Date()
    } catch (e: Exception) {
        e.printStackTrace()
        Date()
    }
}