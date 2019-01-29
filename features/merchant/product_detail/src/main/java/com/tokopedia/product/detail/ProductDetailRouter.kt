package com.tokopedia.product.detail

import android.content.Context
import android.content.Intent

interface ProductDetailRouter{
    fun getLoginIntent(context: Context): Intent
    fun getCartIntent(context: Context): Intent
    fun getProductTalk(context: Context, productId: String): Intent
    fun getProductReputationIntent(context: Context, productId: String, productName: String): Intent
}