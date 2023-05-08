package com.tokopedia.product.detail.common

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

object PostAtcHelper {

    /**
     * Copy of Param in PostAtcActivity.kt
     */
    private const val PARAM_CART_ID = "cartID"
    private const val PARAM_LAYOUT_ID = "layoutID"
    private const val PARAM_PAGE_SOURCE = "pageSource"

    fun start(
        context: Context,
        productId: String,
        layoutId: String = "",
        cartId: String = "",
        pageSource: Source = Source.Default
    ) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.POST_ATC,
            productId
        )
        intent.putExtra(PARAM_CART_ID, cartId)
        intent.putExtra(PARAM_LAYOUT_ID, layoutId)
        intent.putExtra(PARAM_PAGE_SOURCE, pageSource.name)
        context.startActivity(intent)
    }

    sealed class Source(
        open val name: String
    ) {
        object PDP : Source("product detail page")
        object Default : Source("")
    }

}
