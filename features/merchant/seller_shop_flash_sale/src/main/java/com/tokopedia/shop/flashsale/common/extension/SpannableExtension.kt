package com.tokopedia.shop.flashsale.common.extension

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BulletSpan

private const val GAP_WIDTH = 16
private const val START_SPAN = 0
private const val END_SPAN = 0
fun SpannableString.toBulletSpan(): SpannableString {
    this.setSpan(
        BulletSpan(GAP_WIDTH, Color.BLACK),
        START_SPAN,
        END_SPAN,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return this
}