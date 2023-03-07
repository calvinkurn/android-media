package com.tokopedia.tokopedianow.similarproduct.adapter

import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel

interface SimilarProductTypeFactory {

    fun type(uiModel: SimilarProductUiModel): Int
}
