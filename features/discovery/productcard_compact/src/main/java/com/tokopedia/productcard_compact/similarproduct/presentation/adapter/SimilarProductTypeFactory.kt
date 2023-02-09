package com.tokopedia.productcard_compact.similarproduct.presentation.adapter

import com.tokopedia.productcard_compact.similarproduct.presentation.uimodel.SimilarProductUiModel

interface SimilarProductTypeFactory {
    fun type(uiModel: SimilarProductUiModel): Int
}
