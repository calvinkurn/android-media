package com.tokopedia.cart.view.uimodel.new

data class CartShopSimpleHolderData(
        var isTokoNow: Boolean = false,
        var isChecked: Boolean = false,
        var shopName: String = "",
        var shopBadgeUrl: String = "",
        var imageFulfilmentUrl: String = "",
        var shopLocation: String = "",
        var estimatedTimeArrival: String = "",
        var preOrderInfo: String = "",
        var freeShippingUrl: String = "",
        var incidentInfo: String = ""
)