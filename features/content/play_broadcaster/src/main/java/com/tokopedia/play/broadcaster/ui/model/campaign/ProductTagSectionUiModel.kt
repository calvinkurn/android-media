package com.tokopedia.play.broadcaster.ui.model.campaign

import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel

/**
 * Created By : Jonathan Darwin on February 08, 2022
 */
data class ProductTagSectionUiModel(
    val name: String,
    val statusFmt: String,
    val products: List<ProductUiModel>,
)