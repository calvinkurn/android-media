package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.logisticcart.shipping.model.ShopShipment

data class OrderShop(
        var shopId: Int = 0,
        var userId: Int = 0,
        var shopName: String = "",
        var shopBadge: String = "",
        var shopUrl: String = "",
        var isGold: Int = 0,
        var isOfficial: Int = 0,
        var addressId: Int = 0,
        var postalCode: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var districtId: Int = 0,
        var districtName: String = "",
        var origin: Int = 0,
        var addressStreet: String = "",
        var provinceId: Int = 0,
        var cityId: Int = 0,
        var cityName: String = "",
        var isFulfillment: Boolean = false,
        var fulfillmentBadgeUrl: String = "",
        var shopShipment: List<ShopShipment> = emptyList(),
        var errors: List<String> = emptyList()
)