package com.tokopedia.favorite.view.viewmodel

/**
 * @author by erry on 30/01/17.
 */
data class TopAdsShopItem(
        var shopId: String? = null,
        var shopName: String? = null,
        var shopCoverUrl: String? = null,
        var shopCoverEcs: String? = null,
        var shopImageUrl: String? = null,
        var shopImageEcs: String? = null,
        var shopLocation: String? = null,
        var isFav: Boolean = false,
        var shopClickUrl: String? = null,
        var adKey: String? = null,
        var shopDomain: String? = null
)
