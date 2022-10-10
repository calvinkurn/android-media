package com.tokopedia.buyerorderdetail.domain.models

import java.io.Serializable

sealed interface GetP1DataRequestState : Serializable {

    val type: String
    val getOrderResolutionRequestState: GetOrderResolutionRequestState
    val getInsuranceDetailRequestState: GetInsuranceDetailRequestState

    data class Requesting(
        override val getOrderResolutionRequestState: GetOrderResolutionRequestState = GetOrderResolutionRequestState.Requesting,
        override val getInsuranceDetailRequestState: GetInsuranceDetailRequestState = GetInsuranceDetailRequestState.Requesting,
    ) : GetP1DataRequestState {
        override val type: String = Requesting::class.java.simpleName
    }

    data class Complete(
        override val getOrderResolutionRequestState: GetOrderResolutionRequestState,
        override val getInsuranceDetailRequestState: GetInsuranceDetailRequestState,
    ) : GetP1DataRequestState {
        fun getThrowable(): Throwable {
            return if (getOrderResolutionRequestState is GetOrderResolutionRequestState.Error) {
                getOrderResolutionRequestState.throwable
            } else if (getInsuranceDetailRequestState is GetInsuranceDetailRequestState.Error) {
                getInsuranceDetailRequestState.throwable
            } else Throwable()
        }

        override val type: String = Complete::class.java.simpleName
    }
}
