package com.tokopedia.recharge_credit_card

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.annotation.ColorRes

const val PATH_CREDIT_CARD = "digital/creditcard/iframe"

fun Resources.getColorFromResources(context: Context, @ColorRes resId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.getColor(resId, context.theme)
    } else {
        this.getColor(resId)
    }
}