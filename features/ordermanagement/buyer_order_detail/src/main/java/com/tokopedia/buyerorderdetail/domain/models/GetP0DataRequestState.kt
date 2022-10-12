package com.tokopedia.buyerorderdetail.domain.models

import java.io.Serializable

sealed class GetP0DataRequestState : Serializable {

    abstract val type: String

    interface CompleteState

    data class Requesting(
        val getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState = GetBuyerOrderDetailRequestState.Requesting
    ) : GetP0DataRequestState() {
        override val type: String = Requesting::class.java.simpleName
    }

    data class Success(
        val getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState.Success,
    ) : GetP0DataRequestState(), CompleteState {
        override val type: String = Success::class.java.simpleName
    }

    data class Error(
        val getBuyerOrderDetailRequestState: GetBuyerOrderDetailRequestState,
    ) : GetP0DataRequestState(), CompleteState {
        fun getThrowable(): Throwable {
            return if (getBuyerOrderDetailRequestState is GetBuyerOrderDetailRequestState.Error) {
                getBuyerOrderDetailRequestState.throwable
            } else Throwable()
        }

        override val type: String = Error::class.java.simpleName
    }
}
