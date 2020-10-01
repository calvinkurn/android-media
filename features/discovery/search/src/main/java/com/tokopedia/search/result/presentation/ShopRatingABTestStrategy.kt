package com.tokopedia.search.result.presentation

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.topads.sdk.domain.model.Data

interface ShopRatingABTestStrategy {

    fun processShopRatingVariant(productModel: SearchProductModel.Product, productItemViewModel: ProductItemViewModel)

    fun processShopRatingVariant(topAdsData: Data, productItemViewModel: ProductItemViewModel)
}