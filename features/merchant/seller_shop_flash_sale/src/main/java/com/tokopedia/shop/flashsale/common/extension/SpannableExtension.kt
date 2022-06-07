package com.tokopedia.shop.flashsale.common.extension

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BulletSpan

fun SpannableString.toBulletSpan(): SpannableString {
    this.setSpan(
            BulletSpan(16, Color.BLACK),
            0,
            0,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    return this
}