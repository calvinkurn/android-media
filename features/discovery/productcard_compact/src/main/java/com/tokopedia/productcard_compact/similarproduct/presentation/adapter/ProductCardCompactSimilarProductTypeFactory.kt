package com.tokopedia.productcard_compact.similarproduct.presentation.adapter

import com.tokopedia.productcard_compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel

interface ProductCardCompactSimilarProductTypeFactory {
    fun type(uiModel: ProductCardCompactSimilarProductUiModel): Int
}
