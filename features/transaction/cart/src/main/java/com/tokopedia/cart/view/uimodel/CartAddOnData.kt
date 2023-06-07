package com.tokopedia.cart.view.uimodel

import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopTypeInfo
import com.tokopedia.logisticcart.shipping.model.ShopShipment

data class CartAddOnData(
        var listData: List<CartAddOnProductData> = emptyList(),
        var widget: CartAddOnWidgetData = CartAddOnWidgetData()
)
