package com.tokopedia.cartrevamp.view.uimodel

data class CartAddOnData(
    var listData: ArrayList<CartAddOnProductData> = arrayListOf(),
    var widget: CartAddOnWidgetData = CartAddOnWidgetData()
)
