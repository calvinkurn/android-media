package com.tokopedia.shop.home.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop.common.util.ShopProductViewGridType

/**
 * Created by zulfikarrahman on 1/16/18.
 */

data class ShopHomeProductChangeGridSectionUiModel(
        var totalProduct: Int = 0,
        var gridType: ShopProductViewGridType = ShopProductViewGridType.SMALL_GRID
) : Visitable<ShopHomeAdapterTypeFactory> {

    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
