package com.tokopedia.thankyou_native.helper

import android.widget.LinearLayout

fun String.getMaskedNumberSubStringPayment(): String {
    val LAST_NUMBERS = 4
    val FOUR_CROSS = "XXXX "
    return FOUR_CROSS + this.substring(this.length - LAST_NUMBERS)
}
