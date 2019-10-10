package com.tokopedia.search.result.shop.presentation.mapper

import com.tokopedia.discovery.common.Mapper
import com.tokopedia.search.result.shop.domain.model.SearchShopModel
import com.tokopedia.search.result.shop.presentation.model.ShopTotalCountViewModel

class ShopTotalCountViewModelMapper: Mapper<SearchShopModel, ShopTotalCountViewModel> {

    override fun convert(source: SearchShopModel): ShopTotalCountViewModel {
        val searchShopData = source.aceSearchShop

        return ShopTotalCountViewModel(
                totalShopCount = searchShopData.totalShop,
                isAdsBannerVisible = isCpmModelDataSizeMoreThanZero(source) && isCpmDataHasCpmShop(source)
        )
    }

    private fun isCpmModelDataSizeMoreThanZero(source: SearchShopModel): Boolean {
        return source.cpmModel.data?.size ?: 0 > 0
    }

    private fun isCpmDataHasCpmShop(source: SearchShopModel): Boolean {
        return source.cpmModel.data[0].cpm.cpmShop != null
    }
}