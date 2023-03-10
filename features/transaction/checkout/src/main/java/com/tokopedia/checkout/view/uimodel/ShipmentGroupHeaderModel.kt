package com.tokopedia.checkout.view.uimodel

import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel

data class ShipmentGroupHeaderModel(
    val shipmentCartItem: ShipmentCartItemModel = ShipmentCartItemModel()
)
