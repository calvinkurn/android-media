package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView
import com.tokopedia.productcard.compact.similarproduct.presentation.listener.ProductCardCompactSimilarProductTrackerListener

class ProductCardCompactCallback (
    private val similarProductClicked: (productId: String, similarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener?) -> Unit
): ProductCardCompactView.ProductCardCompactListener {
    override fun onClickSimilarProduct(
        productId: String,
        similarProductTrackerListener: ProductCardCompactSimilarProductTrackerListener?
    ) {
        similarProductClicked(productId, similarProductTrackerListener)
    }
}
