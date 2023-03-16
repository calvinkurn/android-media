package com.tokopedia.checkout.view.uimodel

import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnWordingModel

data class ShipmentGroupProductExpandModel(
    val shipmentCartItem: ShipmentCartItemModel,
    val addOnWording: AddOnWordingModel? = null,
)
