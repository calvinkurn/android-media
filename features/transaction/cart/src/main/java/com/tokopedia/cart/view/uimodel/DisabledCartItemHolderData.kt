package com.tokopedia.cart.view.uimodel

import com.tokopedia.cart.domain.model.cartlist.ActionData
import com.tokopedia.cart.domain.model.cartlist.SimilarProductData
import com.tokopedia.cart.domain.model.cartlist.CartItemData
import com.tokopedia.cart.domain.model.cartlist.NicotineLiteMessageData

data class DisabledCartItemHolderData(
        var cartId: Int = 0,
        var productId: String = "",
        var productImage: String = "",
        var productName: String = "",
        var productPrice: Double = 0.0,
        var isWishlisted: Boolean = false,
        var tickerMessage: String? = null,
        var similarProductUrl: String = "",
        var nicotineLiteMessageData: NicotineLiteMessageData? = null,
        var showDivider: Boolean = true,
        var data: CartItemData? = CartItemData(),
        var actionsData: List<ActionData> = emptyList()
)