package com.tokopedia.buyerorderdetail.domain.models

sealed interface GetP1DataRequestState {
    val getOrderResolutionRequestState: GetOrderResolutionRequestState

    data class Requesting(
        override val getOrderResolutionRequestState: GetOrderResolutionRequestState = GetOrderResolutionRequestState.Requesting
    ) : GetP1DataRequestState

    data class Complete(
        override val getOrderResolutionRequestState: GetOrderResolutionRequestState
    ) : GetP1DataRequestState {
        fun getThrowable(): Throwable? {
            return if (getOrderResolutionRequestState is GetOrderResolutionRequestState.Complete.Error) {
                getOrderResolutionRequestState.throwable
            } else null
        }
    }
}
