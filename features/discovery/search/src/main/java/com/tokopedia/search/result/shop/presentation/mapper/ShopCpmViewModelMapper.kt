package com.tokopedia.search.result.shop.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopCpmViewModel

internal class ShopCpmViewModelMapper: Mapper<SearchShopModel, ShopCpmViewModel> {

    override fun convert(source: SearchShopModel): ShopCpmViewModel {
        return ShopCpmViewModel(source.cpmModel)
    }
}