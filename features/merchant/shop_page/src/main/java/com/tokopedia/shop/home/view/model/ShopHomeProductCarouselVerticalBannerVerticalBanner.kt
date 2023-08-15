package com.tokopedia.shop.home.view.model

data class ShopHomeProductCarouselVerticalBannerVerticalBanner(
    val imageUrl: String,
    val bannerType: String,
    val appLink: String,
    override val id : String = imageUrl
) : ShopHomeProductCarouselVerticalBannerItemType
