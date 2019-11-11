package com.tokopedia.purchase_platform.features.cart.view.viewmodel

import com.tokopedia.purchase_platform.common.feature.promo_suggestion.SimilarProductData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData

data class DisabledCartItemHolderData(
        var cartId: Int = 0,
        var productId: String = "",
        var productImage: String = "",
        var productName: String = "",
        var productPrice: Double = 0.0,
        var error: String? = null,
        var isWishlisted: Boolean = false,
        var tickerMessage: String? = null,
        var similarProduct: SimilarProductData? = null,
        var showDivider: Boolean = true,
        var data: CartItemData = CartItemData()
)