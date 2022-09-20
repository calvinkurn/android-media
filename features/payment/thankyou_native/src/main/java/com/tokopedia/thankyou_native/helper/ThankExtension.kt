package com.tokopedia.thankyou_native.helper

import android.widget.LinearLayout

fun String.getMaskedNumberSubStringPayment(): String {
    val LAST_NUMBERS = 4
    val FOUR_CROSS = "\u00b7\u00b7\u00b7\u00b7 "
    return FOUR_CROSS + this.substring(this.length - LAST_NUMBERS)
}
