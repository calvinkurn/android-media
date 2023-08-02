package com.tokopedia.cart.view.uimodel

import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopTypeInfo
import com.tokopedia.logisticcart.shipping.model.ShopShipment

data class CartShopHolderData(
    var shopId: String = "",
    var shopName: String = "",
    var shopTypeInfo: ShopTypeInfo = ShopTypeInfo(),
    var isTokoNow: Boolean = false,
    var incidentInfo: String = "",
    var maximumWeightWording: String = "",
    var shopShipments: List<ShopShipment> = emptyList(),
    var districtId: String = "",
    var postalCode: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var poDuration: String = "",
    var enablerLabel: String = ""
)
