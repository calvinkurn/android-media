package com.tokopedia.product.detail

import android.content.Context
import android.content.Intent

interface ProductDetailRouter{
    fun getLoginIntent(context: Context): Intent
    fun getCartIntent(context: Context): Intent
    fun getProductTalk(context: Context, productId: String): Intent
    fun getProductReputationIntent(context: Context, productId: String, productName: String): Intent
    fun getShoProductListIntent(context: Context, shopId: String, keyword: String, etalaseId: String): Intent
    fun getShopPageIntent(context: Context, shopId: String): Intent
    fun goToEditProduct(context: Context, isEdit: Boolean, productId: String): Intent
}