package com.tokopedia.search.result.shop.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopCpmDataView

internal class ShopCpmViewModelMapper: Mapper<SearchShopModel, ShopCpmDataView> {

    override fun convert(source: SearchShopModel): ShopCpmDataView {
        return ShopCpmDataView(source.cpmModel)
    }
}