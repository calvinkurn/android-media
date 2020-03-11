package com.tokopedia.shop.home.view.model

import com.tokopedia.atc_common.domain.model.response.DataModel

data class ShopHomeAddToCartSuccessDataModel(
        val parentPosition: Int = 0,
        val shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel = ShopHomeCarousellProductUiModel(),
        val shopHomeProductViewModel: ShopHomeProductViewModel = ShopHomeProductViewModel(),
        val dataModel: DataModel = DataModel()
)