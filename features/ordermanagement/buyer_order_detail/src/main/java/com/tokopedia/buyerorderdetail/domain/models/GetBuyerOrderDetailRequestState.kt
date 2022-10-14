package com.tokopedia.buyerorderdetail.domain.models

sealed interface GetBuyerOrderDetailRequestState {
    object Requesting : GetBuyerOrderDetailRequestState
    sealed interface Complete : GetBuyerOrderDetailRequestState {
        data class Success(val result: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail) : Complete
        data class Error(val throwable: Throwable?) : Complete
    }
}
