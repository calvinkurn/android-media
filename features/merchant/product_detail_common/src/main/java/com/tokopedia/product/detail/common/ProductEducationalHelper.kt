package com.tokopedia.product.detail.common

import android.content.Context
import com.tokopedia.applink.RouteManager

object ProductEducationalHelper {
    const val PRODUCT_ID_ARGS = "product_id"
    const val SHOP_ID_ARGS = "shop_id"

    fun goToEducationalBottomSheet(context: Context, url: String, productId: String, shopId: String) {
        val intent = RouteManager.getIntent(context, url)
        intent.putExtra(PRODUCT_ID_ARGS, productId)
        intent.putExtra(SHOP_ID_ARGS, shopId)

        context.startActivity(intent)
    }
}