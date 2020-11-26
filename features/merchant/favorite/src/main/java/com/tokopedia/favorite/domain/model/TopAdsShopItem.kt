package com.tokopedia.favorite.domain.model

/**
 * @author kulomady on 1/24/17.
 */
data class TopAdsShopItem (
        var id: String? = null,
        var adRefKey: String? = null,
        var redirect: String? = null,
        var shopClickUrl: String? = null,
        var shopId: String? = null,
        var shopLocation: String? = null,
        var goldShop: String? = null,
        var shopName: String? = null,
        var luckyShop: String? = null,
        var shopUri: String? = null,
        var shopImageCover: String? = null,
        var shopImageCoverEcs: String? = null,
        var shopImageUrl: String? = null,
        var shopImageEcs: String? = null,
        var shopDomain: String? = null,
        var isSelected: Boolean = false
)
