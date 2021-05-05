package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.logisticcart.shipping.model.ShopShipment

data class OrderShop(
        var shopId: Long = 0,
        var userId: Long = 0,
        var shopName: String = "",
        var shopBadge: String = "",
        var shopTier: Int = 0,
        var shopTypeName: String = "",
        var isGold: Int = 0,
        var isOfficial: Int = 0,
        var postalCode: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var districtId: String = "",
        var cityName: String = "",
        var isFulfillment: Boolean = false,
        var fulfillmentBadgeUrl: String = "",
        var shopShipment: List<ShopShipment> = emptyList(),
        var errors: List<String> = emptyList()
)