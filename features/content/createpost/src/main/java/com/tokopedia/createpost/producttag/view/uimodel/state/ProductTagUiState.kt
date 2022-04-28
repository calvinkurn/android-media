package com.tokopedia.createpost.producttag.view.uimodel.state

import com.tokopedia.createpost.producttag.view.uimodel.PagedState
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on April 25, 2022
 */
data class ProductTagUiState(
    val productTagSource: ProductTagSourceUiState,
    val lastTaggedProduct: LastTaggedProductUiState,
    val lastPurchasedProduct: LastPurchasedProductUiState,
)

data class ProductTagSourceUiState(
    val productTagSourceList: List<ProductTagSource>,
    val selectedProductTagSource: ProductTagSource,
)

data class LastTaggedProductUiState(
    val products: List<ProductUiModel>,
    val nextCursor: String,
    val state: PagedState,
)

data class LastPurchasedProductUiState(
    val products: List<ProductUiModel>,
    val nextCursor: String,
    val state: PagedState,
    val coachmark: String,
    val isCoachmarkShown: Boolean,
)