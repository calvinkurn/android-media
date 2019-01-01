package com.tokopedia.expresscheckout.domain.model.atc

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class ShopModel(
        var shopId: Int = 0,
        var userId: Int = 0,
        var shopName: String? = null,
        var shopImage: String? = null,
        var shopUrl: String? = null,
        var shopStatus: Int = 0,
        var isGold: Int = 0,
        var isGoldBadge: Boolean = false,
        var isOfficial: Int = 0,
        var isFreeReturns: Int = 0,
        var addressId: Int = 0,
        var postalCode: String? = null,
        var latitude: String? = null,
        var longitude: String? = null,
        var districtId: Int = 0,
        var districtName: String? = null,
        var origin: Int = 0,
        var addressStreet: String? = null,
        var provinceId: Int = 0,
        var cityId: Int = 0,
        var cityName: String? = null
)