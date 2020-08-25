package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ShopRatingABTest
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel

class ShopRatingABTestVariantB: ShopRatingABTest {

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
        productItemViewModel.shopRating = productModel.shop.ratingAverage

        if (productModel.countSold.isNotEmpty()) {
            productItemViewModel.labelGroupList.removeAll { it.isLabelIntegrity() }
            productItemViewModel.labelGroupList.add(createLabelIntegrityFromCountSold(productModel))
        }
    }

    private fun createLabelIntegrityFromCountSold(productModel: SearchProductModel.Product) =
            LabelGroupViewModel(ProductCardLabel.LABEL_INTEGRITY, ProductCardLabel.LABEL_INTEGRITY_TYPE, productModel.countSold)
}