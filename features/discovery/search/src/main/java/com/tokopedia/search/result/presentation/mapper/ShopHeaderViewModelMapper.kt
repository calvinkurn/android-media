package com.tokopedia.search.result.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.search.result.domain.model.SearchShopModel
import com.tokopedia.search.result.presentation.model.ShopHeaderViewModel

class ShopHeaderViewModelMapper: Mapper<SearchShopModel, ShopHeaderViewModel> {

    override fun convert(source: SearchShopModel): ShopHeaderViewModel {
        val searchShopData = source.aceSearchShop

        return ShopHeaderViewModel(
                cpmModel = source.cpmModel,
                totalShopCount = searchShopData.totalShop
        )
    }
}