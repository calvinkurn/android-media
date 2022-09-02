package com.tokopedia.recharge_credit_card

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.Editable
import androidx.annotation.ColorRes

const val PATH_CREDIT_CARD = "digital/creditcard/iframe"

fun Resources.getColorFromResources(context: Context, @ColorRes resId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.getColor(resId, context.theme)
    } else {
        this.getColor(resId)
    }
}

internal fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

internal fun String.isMasked(): Boolean = this.contains("*")