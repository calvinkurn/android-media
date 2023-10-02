package com.tokopedia.shop.home.view.model.banner_product_group.appearance

import com.tokopedia.shop.common.view.model.ShopPageColorSchema

data class ShimmerItemType(
    val showShimmer: Boolean,
    override val overrideTheme: Boolean = false,
    override val colorSchema: ShopPageColorSchema = ShopPageColorSchema(),
    override val id: String = ""
) : ShopHomeBannerProductGroupItemType
