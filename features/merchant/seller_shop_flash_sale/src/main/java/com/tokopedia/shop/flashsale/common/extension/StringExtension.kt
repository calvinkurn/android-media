package com.tokopedia.shop.flashsale.common.extension

import android.graphics.Color
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

fun String.isValidHexColor(): Boolean {
    return try {
        Color.parseColor(this)
        true
    } catch (e: Exception) {
        false
    }
}

fun String.toColor(): Int {
    return try {
        Color.parseColor(this)
    } catch (e: Exception) {
        Color.WHITE
    }
}

fun String.toHexColor() : String{
    return "#${this}"
}

fun String.removeHexColorPrefix() : String {
    return removePrefix("#")
}

fun String.isNumber(): Boolean {
    val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
    return this.matches(regex)
}

fun String.filterDigit() = filter { it.isDigit() }