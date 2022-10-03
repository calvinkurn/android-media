package com.tokopedia.buyerorderdetail.domain.models

import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.usecase.coroutines.Result
import java.io.Serializable

sealed class AddToCartSingleRequestState : Serializable {

    abstract val type: String

    object Requesting : AddToCartSingleRequestState() {
        override val type: String = Requesting::class.java.simpleName
    }

    data class Success(
        val result: Pair<ProductListUiModel.ProductUiModel, Result<AtcMultiData>>
    ) : AddToCartSingleRequestState() {
        override val type: String = Success::class.java.simpleName
    }

    data class Error(val throwable: Throwable) : AddToCartSingleRequestState() {
        override val type: String = Error::class.java.simpleName
    }
}
