package com.tokopedia.cartrevamp.view.uimodel

data class CartAddOnData(
    var listData: List<CartAddOnProductData> = emptyList(),
    var widget: CartAddOnWidgetData = CartAddOnWidgetData()
)
