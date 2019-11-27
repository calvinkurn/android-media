package com.tokopedia.product.detail

import android.content.Context
import com.tokopedia.transaction.common.TransactionRouter

interface ProductDetailRouter{
    fun getDeviceId(context : Context): String
}