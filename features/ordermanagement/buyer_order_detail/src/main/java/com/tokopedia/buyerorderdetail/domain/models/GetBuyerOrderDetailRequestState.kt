package com.tokopedia.buyerorderdetail.domain.models

sealed class GetBuyerOrderDetailRequestState {

    object Requesting : GetBuyerOrderDetailRequestState()

    data class Success(
        val result: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ) : GetBuyerOrderDetailRequestState()

    data class Error(val throwable: Throwable?) : GetBuyerOrderDetailRequestState()
}
