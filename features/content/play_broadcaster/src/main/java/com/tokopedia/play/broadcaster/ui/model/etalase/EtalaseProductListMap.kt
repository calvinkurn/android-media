package com.tokopedia.play.broadcaster.ui.model.etalase

import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel

/**
 * Created by kenny.hadisaputra on 04/02/22
 */
typealias EtalaseProductListMap = Map<ProductSectionKey, List<ProductUiModel>>

data class ProductSectionKey(
    val title: String,
    val status: String,
)