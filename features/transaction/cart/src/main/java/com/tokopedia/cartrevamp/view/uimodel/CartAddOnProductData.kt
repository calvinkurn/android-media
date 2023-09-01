package com.tokopedia.cartrevamp.view.uimodel

data class CartAddOnProductData(
    var id: String = "",
    var uniqueId: String = "",
    var status: Int = -1,
    var type: Int = -1,
    var price: Double = 0.0
)
