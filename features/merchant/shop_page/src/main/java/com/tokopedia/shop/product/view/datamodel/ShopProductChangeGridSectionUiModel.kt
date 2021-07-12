package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

/**
 * Created by zulfikarrahman on 1/16/18.
 */

data class ShopProductChangeGridSectionUiModel(
        var totalProduct: Int = 0,
        var gridType: ShopProductViewGridType = ShopProductViewGridType.SMALL_GRID
) : BaseShopProductViewModel {

    override fun type(typeFactory: ShopProductAdapterTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}
