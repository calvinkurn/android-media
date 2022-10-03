package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel

sealed class ProductListUiState {
    object Loading : ProductListUiState()

    data class Showing(
        val data: ProductListUiModel
    ) : ProductListUiState()

    data class Error(
        val throwable: Throwable
    ) : ProductListUiState()
}
