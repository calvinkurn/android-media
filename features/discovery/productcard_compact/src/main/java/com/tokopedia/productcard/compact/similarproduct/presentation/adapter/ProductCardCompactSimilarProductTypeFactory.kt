package com.tokopedia.productcard.compact.similarproduct.presentation.adapter

import com.tokopedia.productcard.compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel

interface ProductCardCompactSimilarProductTypeFactory {
    fun type(uiModel: ProductCardCompactSimilarProductUiModel): Int
}
