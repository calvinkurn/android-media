package com.tokopedia.shop.home.view.model

import com.tokopedia.shop.home.view.model.ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data.BannerType

data class ShopHomeProductCarouselVerticalBannerVerticalBanner(
    val imageUrl: String,
    val bannerType: BannerType,
    val appLink: String,
    override val id : String = imageUrl
) : ShopHomeProductCarouselVerticalBannerItemType
