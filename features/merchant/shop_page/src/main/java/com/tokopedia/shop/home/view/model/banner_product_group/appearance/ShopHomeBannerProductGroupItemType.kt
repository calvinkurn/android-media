package com.tokopedia.shop.home.view.model.banner_product_group.appearance

import com.tokopedia.shop.common.view.model.ShopPageColorSchema

sealed interface ShopHomeBannerProductGroupItemType {
    val id: String
    val overrideTheme: Boolean
    val colorSchema: ShopPageColorSchema
}



