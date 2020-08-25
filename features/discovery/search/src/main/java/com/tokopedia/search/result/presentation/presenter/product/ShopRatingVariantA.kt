package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ShopRatingABTest
import com.tokopedia.search.result.presentation.model.ProductItemViewModel

class ShopRatingABTestVariantA: ShopRatingABTest {

    override fun processShopRatingVariant(productModel: SearchProductModel.Product, productItemViewModel: ProductItemViewModel) {
        try {
            tryProcessShopRatingVariant(productItemViewModel, productModel)
        }
        catch (ignored: Exception) { }
    }

    private fun tryProcessShopRatingVariant(productItemViewModel: ProductItemViewModel, productModel: SearchProductModel.Product) {
        if (productItemViewModel.willShowRatingAndReview())
            productItemViewModel.labelGroupList.removeAll { it.isLabelIntegrity() }
        else if (productItemViewModel.doesNotHaveLabelIntegrity())
            productItemViewModel.shopRating = productModel.shop.ratingAverage
    }

    private fun ProductItemViewModel.doesNotHaveLabelIntegrity() = !labelGroupList.any { it.isLabelIntegrity() }
}