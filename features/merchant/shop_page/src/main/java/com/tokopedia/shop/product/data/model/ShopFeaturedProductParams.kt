package com.tokopedia.shop.product.data.model

data class ShopFeaturedProductParams(
        val shopId: String = "",
        val userId: String = "",
        var districtId: String = "",
        var cityId: String = "",
        var latitude: String = "",
        var longitude: String = ""
)