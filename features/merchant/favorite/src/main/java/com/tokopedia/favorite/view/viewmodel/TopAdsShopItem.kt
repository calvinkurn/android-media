package com.tokopedia.favorite.view.viewmodel

import com.tokopedia.topads.sdk.domain.model.ImageShop

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
        var shopDomain: String? = null,
        var imageUrl: String? = "",
        var fullEcs: String? = "",
        var shopIsOfficial: Boolean = false,
        var isPMPro: Boolean = false,
        var isPowerMerchant: Boolean = false,
        var imageShop: ImageShop?  = null,
        var layout: Int?  = null,
        var applink: String? = "",
        var isFollowed: Boolean = false
)
