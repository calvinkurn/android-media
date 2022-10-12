package com.tokopedia.buyerorderdetail.domain.models

import java.io.Serializable

sealed class GetBuyerOrderDetailDataRequestState : Serializable {

    abstract val type: String

    interface CompleteState
    interface Started {
        val getP0DataRequestState: GetP0DataRequestState
        val getP1DataRequestState: GetP1DataRequestState
    }

    object Idle : GetBuyerOrderDetailDataRequestState() {
        override val type: String = Idle::class.java.simpleName
    }

    data class Requesting(
        override val getP0DataRequestState: GetP0DataRequestState,
        override val getP1DataRequestState: GetP1DataRequestState,
    ) : GetBuyerOrderDetailDataRequestState(), Started {
        override val type: String = Requesting::class.java.simpleName
    }

    data class Success(
        override val getP0DataRequestState: GetP0DataRequestState,
        override val getP1DataRequestState: GetP1DataRequestState,
    ) : GetBuyerOrderDetailDataRequestState(), CompleteState, Started {
        override val type: String = Success::class.java.simpleName
    }

    data class Error(
        override val getP0DataRequestState: GetP0DataRequestState,
        override val getP1DataRequestState: GetP1DataRequestState,
    ) : GetBuyerOrderDetailDataRequestState(), CompleteState, Started {
        fun getThrowable(): Throwable {
            return if (getP0DataRequestState is GetP0DataRequestState.Error) {
                getP0DataRequestState.getThrowable()
            } else Throwable()
        }

        override val type: String = Error::class.java.simpleName
    }
}

