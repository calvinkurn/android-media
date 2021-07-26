package com.tokopedia.shop.pageheader.data.model

import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster

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
        var shopStatus: Int = -1,
        var broadcaster: Broadcaster.Config = Broadcaster.Config(),
        var shopSnippetUrl: String = "",
        var shopCoreUrl: String = "",
        var shopBadge: String = ""
)