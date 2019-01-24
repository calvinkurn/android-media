package com.tokopedia.product.detail

import android.content.Context
import com.tokopedia.product.detail.view.activity.ProductDetailActivity

object ProductDetailInternalRouter{

    @JvmStatic
    fun getProductDetailIntent(context: Context, productUrl: String) = ProductDetailActivity.createIntent(context, productUrl)

}