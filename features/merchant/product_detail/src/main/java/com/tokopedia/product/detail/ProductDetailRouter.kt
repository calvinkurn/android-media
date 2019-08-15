package com.tokopedia.product.detail

import android.content.Context
import com.tokopedia.transaction.common.TransactionRouter

interface ProductDetailRouter{
    fun getCartCount(context: Context): Int
    fun getDeviceId(context : Context): String
}