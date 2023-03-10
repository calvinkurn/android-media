package com.tokopedia.checkout.view.uimodel

import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel

data class ShipmentGroupProductExpandModel(
    val shipmentCartItem: ShipmentCartItemModel = ShipmentCartItemModel()
)
