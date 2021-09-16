package com.tokopedia.purchase_platform.common.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.annotation.Keep

object Switch {

    @Keep
    fun isBundleToggleOn(context: Context): Boolean {
        return context.getSharedPreferences("CART_CHECKOUT_BUNDLING", MODE_PRIVATE).getBoolean("CART_CHECKOUT_BUNDLING", true)
    }

}