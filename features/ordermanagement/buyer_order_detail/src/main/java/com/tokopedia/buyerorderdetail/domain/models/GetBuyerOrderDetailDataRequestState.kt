package com.tokopedia.buyerorderdetail.domain.models

sealed class GetBuyerOrderDetailDataRequestState {

    interface CompleteState
    interface Started {
        val getP0DataRequestState: GetP0DataRequestState
        val getP1DataRequestState: GetP1DataRequestState
    }

    data class Requesting(
        override val getP0DataRequestState: GetP0DataRequestState,
        override val getP1DataRequestState: GetP1DataRequestState,
    ) : GetBuyerOrderDetailDataRequestState(), Started

    data class Success(
        override val getP0DataRequestState: GetP0DataRequestState,
        override val getP1DataRequestState: GetP1DataRequestState,
    ) : GetBuyerOrderDetailDataRequestState(), CompleteState, Started

    data class Error(
        override val getP0DataRequestState: GetP0DataRequestState,
        override val getP1DataRequestState: GetP1DataRequestState,
    ) : GetBuyerOrderDetailDataRequestState(), CompleteState, Started {
        fun getThrowable(): Throwable {
            return if (getP0DataRequestState is GetP0DataRequestState.Error) {
                getP0DataRequestState.getThrowable()
            } else Throwable()
        }
    }
}

