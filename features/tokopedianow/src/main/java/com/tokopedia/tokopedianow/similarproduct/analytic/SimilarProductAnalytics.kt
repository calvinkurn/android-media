package com.tokopedia.tokopedianow.similarproduct.analytic

import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel

interface SimilarProductAnalytics {

    fun trackClickProduct(product: SimilarProductUiModel)

    fun trackClickAddToCart(product: SimilarProductUiModel)

}
