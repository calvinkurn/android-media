package com.tokopedia.shop.flash_sale.common.extension

import android.graphics.Color
import android.text.Spannable
import android.text.style.BulletSpan

fun Spannable.toBulletSpan() {
    this.setSpan(
        BulletSpan(16, Color.BLACK),
        0,
        0,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
}