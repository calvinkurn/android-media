package com.tokopedia.search.result.shop.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopHeaderViewModel

class ShopHeaderViewModelMapper: Mapper<SearchShopModel, ShopHeaderViewModel> {

    override fun convert(source: SearchShopModel): ShopHeaderViewModel {
        val searchShopData = source.aceSearchShop

        return ShopHeaderViewModel(
                cpmModel = source.cpmModel,
                totalShopCount = searchShopData.totalShop
        )
    }
}