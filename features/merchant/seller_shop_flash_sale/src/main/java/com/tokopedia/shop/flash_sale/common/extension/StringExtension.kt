package com.tokopedia.shop.flash_sale.common.extension

import android.graphics.Color
import android.text.Spannable
import android.text.style.BulletSpan
import com.tokopedia.utils.date.DateUtil
import java.text.SimpleDateFormat
import java.util.*

fun String.digitsOnly(): Int {
    return try {
        this.filter { it.isDigit() }.toInt()
    } catch (e: Exception) {
        0
    }
}

fun String.toDate(inputFormat: String): Date {
    return try {
        val format = SimpleDateFormat(inputFormat, DateUtil.DEFAULT_LOCALE)
        format.parse(this) ?: Date()
    } catch (e: Exception) {
        e.printStackTrace()
        Date()
    }
}

fun Spannable.setBulletSpan() {
    this.setSpan(
        BulletSpan(16, Color.BLACK),
        0,
        0,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}