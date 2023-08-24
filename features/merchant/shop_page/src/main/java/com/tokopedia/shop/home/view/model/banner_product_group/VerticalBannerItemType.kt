package com.tokopedia.shop.home.view.model.banner_product_group

import com.tokopedia.shop.home.view.model.ShopWidgetComponentBannerProductGroupUiModel.Tab.ComponentList.Data.BannerType

data class VerticalBannerItemType(
    val imageUrl: String,
    val bannerType: BannerType,
    val appLink: String,
    override val id : String = imageUrl
) : ShopHomeBannerProductGroupItemType
