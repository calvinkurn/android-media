package com.tokopedia.purchase_platform.features.cart.view.viewmodel

import com.tokopedia.purchase_platform.common.feature.promo_suggestion.SimilarProduct

data class CartErrorItemHolderData(
        var productId: Int,
        var productImage: String,
        var productName: String,
        var productPrice: Double,
        var error: String?,
        var isWishlisted: Boolean,
        var tickerMessage: String?,
        var similarProduct: SimilarProduct?,
        var showDivider: Boolean
)