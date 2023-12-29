package com.tokopedia.search.result.mps.addtocart

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery.common.State
import com.tokopedia.search.utils.mvvm.SearchUiState

data class AddToCartState(
    val state: State<AddToCartDataModel>? = null
): SearchUiState {

    fun success(addToCartDataModel: AddToCartDataModel) = copy(
        state = State.Success(addToCartDataModel)
    )

    fun error(addToCartException: Throwable) = copy(
        state = State.Error(
            message = addToCartException.message ?: "",
            throwable = addToCartException,
        )
    )

    fun dismiss() = AddToCartState()
}
