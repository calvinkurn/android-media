package com.tokopedia.product.detail

import android.content.Context

interface ProductDetailRouter{
    fun getDeviceId(context : Context): String
}