package com.tokopedia.cart.view.uimodel

import com.tokopedia.cart.data.model.response.shopgroupsimplified.Action

data class DisabledCartItemHolderData(
        var cartId: String = "",
        var productId: String = "",
        var productImage: String = "",
        var productName: String = "",
        var productPrice: Double = 0.0,
        var isWishlisted: Boolean = false,
        var showDivider: Boolean = true,
        var data: CartItemHolderData? = CartItemHolderData(),
        var actionsData: List<Action> = emptyList(),
        var selectedUnavailableActionId: String = "",
        var selectedUnavailableActionLink: String = "",
        var errorType: String = ""
)