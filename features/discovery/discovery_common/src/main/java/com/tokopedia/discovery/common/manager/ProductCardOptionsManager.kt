@file:JvmName("ProductCardOptionsManager")

package com.tokopedia.discovery.common.manager

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.discovery.common.model.ProductCardOptionsModel

const val PRODUCT_CARD_OPTIONS_MODEL = "product_card_options_model"

fun showProductCardOptions(context: Context) {
    val productCardOptionsApplink = "${ApplinkConstInternalDiscovery.INTERNAL_DISCOVERY}/product-card-options"
    val intent = RouteManager.getIntent(context, productCardOptionsApplink)

    intent.putExtra(PRODUCT_CARD_OPTIONS_MODEL, ProductCardOptionsModel(
            hasSimilarSearch = true,
            hasWishlist = true,
            isWishlisted = false,
            keyword = "samsung",
            productId = "433759643",
            isTopAds = true
    ))

    context.startActivity(intent)
}