package com.tokopedia.buyerorderdetail.presentation.uistate

import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel

sealed interface ProductListUiState {
    object Loading : ProductListUiState

    sealed interface HasData : ProductListUiState {
        val data: ProductListUiModel

        data class Reloading(
            override val data: ProductListUiModel
        ) : HasData

        data class Showing(
            override val data: ProductListUiModel
        ) : HasData
    }

    data class Error(
        val throwable: Throwable?
    ) : ProductListUiState
}
