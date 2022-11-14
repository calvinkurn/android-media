package com.tokopedia.tokopedianow.similarproduct.analytic

import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel

interface ProductAnalytics {

    fun trackImpressionProduct(product: SimilarProductUiModel)

    fun trackClickProduct(product: SimilarProductUiModel)

    fun trackClickAddToCart(product: SimilarProductUiModel)

    fun trackImpressionOutOfStockProduct(product: SimilarProductUiModel)

    fun trackClickRemoveProduct()

    fun trackClickDecreaseQuantity()

    fun trackClickIncreaseQuantity()

    fun trackImpressionSimilarProductBtn()

    fun trackClickSimilarProductBtn()
}
