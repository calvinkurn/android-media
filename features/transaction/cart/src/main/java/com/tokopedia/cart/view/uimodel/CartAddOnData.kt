package com.tokopedia.cart.view.uimodel

data class CartAddOnData(
    var listData: ArrayList<CartAddOnProductData> = arrayListOf(),
    var deselectListData: ArrayList<CartAddOnProductData> = arrayListOf(),
    var widget: CartAddOnWidgetData = CartAddOnWidgetData()
)
