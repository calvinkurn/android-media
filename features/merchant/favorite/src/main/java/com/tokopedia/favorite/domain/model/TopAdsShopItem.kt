package com.tokopedia.favorite.domain.model

import com.tokopedia.topads.sdk.domain.model.ImageShop

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
        var isSelected: Boolean = false,
        var imageUrl: String? = "",
        var fullEcs: String? = "",
        var shopIsOfficial: Boolean = false,
        var isPMPro: Boolean = false,
        var isPowerMerchant: Boolean = false,
        var imageShop: ImageShop?  = null,
        var layout: Int?  = null,
        var applink: String?  = "",
        var isFollowed: Boolean  = false,
)
