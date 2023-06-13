package com.tokopedia.product.detail.common

import android.content.Context
import android.content.Intent
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
        val intent = getIntent(
            context,
            productId,
            layoutId,
            cartId,
            pageSource
        )
        context.startActivity(intent)
    }

    fun getIntent(
        context: Context,
        productId: String,
        layoutId: String = "",
        cartId: String = "",
        pageSource: Source = Source.Default
    ): Intent {
        return RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.POST_ATC,
            productId
        ).apply {
            putExtra(PARAM_CART_ID, cartId)
            putExtra(PARAM_LAYOUT_ID, layoutId)
            putExtra(PARAM_PAGE_SOURCE, pageSource.name)
        }
    }

    sealed class Source(
        open val name: String
    ) {
        object PDP : Source("product detail page")
        object Default : Source("")
    }
}
