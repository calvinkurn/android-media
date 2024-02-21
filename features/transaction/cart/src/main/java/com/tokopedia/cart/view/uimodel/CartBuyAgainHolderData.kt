package com.tokopedia.cart.view.uimodel

data class CartBuyAgainHolderData(
    var hasSentImpressionAnalytics: Boolean = false,
    var buyAgainList: List<CartBuyAgainItemHolderData> = arrayListOf()
)
