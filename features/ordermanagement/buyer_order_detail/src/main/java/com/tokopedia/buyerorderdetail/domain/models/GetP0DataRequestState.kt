package com.tokopedia.buyerorderdetail.domain.models

sealed class GetP0DataRequestState {

    interface CompleteState

    data class Requesting(
        val getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState = GetBuyerOrderDetailRequestState.Requesting
    ) : GetP0DataRequestState()

    data class Success(
        val getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
    ) : GetP0DataRequestState(), CompleteState

    data class Error(
        val getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState,
    ) : GetP0DataRequestState(), CompleteState {
        fun getThrowable(): Throwable {
            return if (getBuyerOrderDetailRequestState is GetBuyerOrderDetailRequestState.Error) {
                getBuyerOrderDetailRequestState.throwable
            } else Throwable()
        }
    }
}
