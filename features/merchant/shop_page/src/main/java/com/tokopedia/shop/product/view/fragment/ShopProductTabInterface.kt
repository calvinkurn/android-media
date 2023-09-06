package com.tokopedia.shop.product.view.fragment

import com.tokopedia.shop.common.view.model.ShopPageColorSchema

interface ShopProductTabInterface {
    fun isOverrideTheme(): Boolean

    fun getShopPageColorSchema(): ShopPageColorSchema

    fun getPatternColorType(): String

}
