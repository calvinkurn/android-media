package com.tokopedia.buyerorderdetail.domain.models

import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.usecase.coroutines.Result

sealed class AddToCartSingleRequestState {

    object Requesting : AddToCartSingleRequestState()

    data class Success(
        val result: Pair<ProductListUiModel.ProductUiModel, Result<AtcMultiData>>
    ) : AddToCartSingleRequestState()

    data class Error(val throwable: Throwable?) : AddToCartSingleRequestState()
}
