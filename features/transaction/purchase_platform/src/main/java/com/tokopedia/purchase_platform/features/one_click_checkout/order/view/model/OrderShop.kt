package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.CartDataResponse

data class OrderShop(
        var shopId: Int = 0,
        var userId: Int = 0,
        var shopName: String = "",
        var shopImage: String = "",
        var shopUrl: String = "",
        var shopStatus: Int = 0,
        var isGold: Int = 0,
        var isGoldBadge: Boolean = false,
        var isOfficial: Int = 0,
        var isFreeReturns: Int = 0,
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
        var shopShipment: List<ShopShipment> = emptyList(),
        var cartResponse: CartDataResponse = CartDataResponse(),
        var errors: List<String> = emptyList()
)