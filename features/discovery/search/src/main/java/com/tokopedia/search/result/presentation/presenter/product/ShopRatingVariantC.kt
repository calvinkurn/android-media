package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ShopRatingABTest
import com.tokopedia.search.result.presentation.model.ProductItemViewModel

class ShopRatingABTestVariantC: ShopRatingABTest {

    override fun processShopRatingVariant(productModel: SearchProductModel.Product, productItemViewModel: ProductItemViewModel) {
        try {
            tryProcessShopRatingVariant(productItemViewModel, productModel)
        }
        catch (ignored: Exception) { }
    }

    private fun tryProcessShopRatingVariant(productItemViewModel: ProductItemViewModel, productModel: SearchProductModel.Product) {
        productItemViewModel.rating = 0
        productItemViewModel.ratingString = ""
        productItemViewModel.countReview = 0
        productItemViewModel.labelGroupList.removeAll { it.isLabelIntegrity() }
        productItemViewModel.shopRating = productModel.shop.ratingAverage
        productItemViewModel.isShopRatingYellow = true
    }
}