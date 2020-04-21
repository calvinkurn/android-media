package com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view.uimodel

import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.domain.model.cartlist.CartItemData
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.domain.model.cartlist.NicotineLiteMessageData
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.domain.model.cartlist.SimilarProductData

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
        var nicotineLiteMessageData: NicotineLiteMessageData? = null,
        var showDivider: Boolean = true,
        var data: CartItemData? = CartItemData()
)