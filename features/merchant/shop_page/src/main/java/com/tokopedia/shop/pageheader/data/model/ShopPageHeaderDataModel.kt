package com.tokopedia.shop.pageheader.data.model

data class ShopPageHeaderDataModel(
        var shopId: String = "",
        var shopName: String = "",
        var isOfficial: Boolean = false,
        var isGoldMerchant: Boolean = false,
        var shopHomeType: String = "",
        var topContentUrl: String = "",
        var avatar: String = "",
        var domain: String = "",
        var location: String = "",
        var isFreeOngkir: Boolean = false,
        var statusTitle: String = "",
        var statusMessage: String = "",
        var shopStatus: Int = -1

)