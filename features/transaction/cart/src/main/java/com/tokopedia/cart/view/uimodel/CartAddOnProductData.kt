package com.tokopedia.cart.view.uimodel

import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopTypeInfo
import com.tokopedia.logisticcart.shipping.model.ShopShipment

data class CartAddOnProductData(
        var id: String = "",
        var uniqueId: String = "",
        var status: Int = -1,
        var type: Int = -1,
        var price: Double = 0.0
)
