package com.tokopedia.search.result.presentation

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel

interface ShopRatingABTest {

    fun processShopRatingVariant(productModel: SearchProductModel.Product, productItemViewModel: ProductItemViewModel)
}