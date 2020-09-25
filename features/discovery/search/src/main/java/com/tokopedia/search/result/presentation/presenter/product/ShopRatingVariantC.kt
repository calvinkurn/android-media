package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ShopRatingABTestStrategy
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.topads.sdk.domain.model.Data

class ShopRatingABTestVariantC: ShopRatingABTestStrategy {

    override fun processShopRatingVariant(productModel: SearchProductModel.Product, productItemViewModel: ProductItemViewModel) {
        try {
            tryProcessShopRatingVariant(productItemViewModel, productModel.shop.ratingAverage)
        }
        catch (ignored: Exception) { }
    }

    private fun tryProcessShopRatingVariant(productItemViewModel: ProductItemViewModel, shopRatingAverage: String) {
        productItemViewModel.rating = 0
        productItemViewModel.ratingString = ""
        productItemViewModel.countReview = 0
        productItemViewModel.labelGroupList.removeAll { it.isLabelIntegrity() }
        productItemViewModel.shopRating = shopRatingAverage
        productItemViewModel.isShopRatingYellow = true
    }

    override fun processShopRatingVariant(topAdsData: Data, productItemViewModel: ProductItemViewModel) {
        try {
            tryProcessShopRatingVariant(productItemViewModel, topAdsData.shop.shopRatingAvg)
        }
        catch (ignored: Exception) { }
    }
}