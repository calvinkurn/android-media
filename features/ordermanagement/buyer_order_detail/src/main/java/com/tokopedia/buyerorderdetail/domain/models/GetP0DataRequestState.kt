package com.tokopedia.buyerorderdetail.domain.models

sealed interface GetP0DataRequestState {
    val getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState

    data class Requesting(
        override val getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState = GetBuyerOrderDetailRequestState.Requesting
    ) : GetP0DataRequestState

    data class Complete(
        override val getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState
    ) : GetP0DataRequestState {
        fun getThrowable(): Throwable? {
            return if (getBuyerOrderDetailRequestState is GetBuyerOrderDetailRequestState.Complete.Error) {
                getBuyerOrderDetailRequestState.throwable
            } else null
        }
    }
}
