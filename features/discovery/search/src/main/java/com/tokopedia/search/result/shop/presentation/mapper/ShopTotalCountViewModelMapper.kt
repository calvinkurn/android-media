package com.tokopedia.search.result.shop.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopTotalCountViewModel

internal class ShopTotalCountViewModelMapper: Mapper<SearchShopModel, ShopTotalCountViewModel> {

    override fun convert(source: SearchShopModel): ShopTotalCountViewModel {
        val searchShopData = source.aceSearchShop

        return ShopTotalCountViewModel(totalShopCount = searchShopData.totalShop)
    }
}