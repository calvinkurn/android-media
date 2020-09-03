package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.ShopRatingABTest
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.topads.sdk.domain.model.Data

class ShopRatingABTestVariantB: ShopRatingABTest {

    override fun processShopRatingVariant(productModel: SearchProductModel.Product, productItemViewModel: ProductItemViewModel) {
        try {
            tryProcessShopRatingVariant(productItemViewModel, productModel.shop.ratingAverage, productModel.countSold)
        }
        catch (ignored: Exception) { }
    }

    private fun tryProcessShopRatingVariant(productItemViewModel: ProductItemViewModel, shopRatingAverage: String, countSold: String) {
        productItemViewModel.rating = 0
        productItemViewModel.ratingString = ""
        productItemViewModel.countReview = 0
        productItemViewModel.shopRating = shopRatingAverage

        if (countSold.isNotEmpty()) {
            productItemViewModel.labelGroupList.removeAll { it.isLabelIntegrity() }
            productItemViewModel.labelGroupList.add(createLabelIntegrityFromCountSold(countSold))
        }
    }

    private fun createLabelIntegrityFromCountSold(countSold: String) =
            LabelGroupViewModel(ProductCardLabel.LABEL_INTEGRITY, ProductCardLabel.LABEL_INTEGRITY_TYPE, countSold)

    override fun processShopRatingVariant(topAdsData: Data, productItemViewModel: ProductItemViewModel) {
        try {
            tryProcessShopRatingVariant(productItemViewModel, topAdsData.shop.shopRatingAvg, topAdsData.product.countSold)
        }
        catch (ignored: Exception) { }
    }
}