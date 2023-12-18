package com.tokopedia.product.detail.mapper

import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1

interface ProductDetailMapper {

    fun mapProductDetailToProductPreview(
        data: DynamicProductInfoP1,
        position: Int,
    ): ProductContentUiModel

}
