package com.tokopedia.shop_showcase.shop_showcase_tab.presentation.fragment

import com.tokopedia.shop.common.view.model.ShopPageColorSchema

interface ShopShowcaseTabInterface {
    fun isOverrideTheme(): Boolean

    fun getShopPageColorSchema(): ShopPageColorSchema

    fun getPatternColorType(): String

}
