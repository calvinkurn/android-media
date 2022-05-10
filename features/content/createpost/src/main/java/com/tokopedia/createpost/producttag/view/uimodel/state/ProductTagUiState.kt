package com.tokopedia.createpost.producttag.view.uimodel.state

import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
data class ProductTagUiState(
    val productTagSource: ProductTagSourceUiState,
)

data class ProductTagSourceUiState(
    val productTagSourceList: List<ProductTagSource>,
    val selectedProductTagSource: ProductTagSource,
)