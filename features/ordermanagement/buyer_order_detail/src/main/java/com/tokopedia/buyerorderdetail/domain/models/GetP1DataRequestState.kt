package com.tokopedia.buyerorderdetail.domain.models

sealed interface GetP1DataRequestState {
    val getOrderResolutionRequestState: GetOrderResolutionRequestState
    val getInsuranceDetailRequestState: GetInsuranceDetailRequestState
    val getBrcCsatWidgetRequestState: GetBrcCsatWidgetRequestState

    data class Requesting(
        override val getOrderResolutionRequestState: GetOrderResolutionRequestState = GetOrderResolutionRequestState.Requesting,
        override val getInsuranceDetailRequestState: GetInsuranceDetailRequestState = GetInsuranceDetailRequestState.Requesting,
        override val getBrcCsatWidgetRequestState: GetBrcCsatWidgetRequestState = GetBrcCsatWidgetRequestState.Requesting
    ) : GetP1DataRequestState

    data class Complete(
        override val getOrderResolutionRequestState: GetOrderResolutionRequestState,
        override val getInsuranceDetailRequestState: GetInsuranceDetailRequestState,
        override val getBrcCsatWidgetRequestState: GetBrcCsatWidgetRequestState,
    ) : GetP1DataRequestState {
        fun getThrowable(): Throwable? {
            return if (getOrderResolutionRequestState is GetOrderResolutionRequestState.Complete.Error) {
                getOrderResolutionRequestState.throwable
            } else if (getInsuranceDetailRequestState is GetInsuranceDetailRequestState.Complete.Error) {
                getInsuranceDetailRequestState.throwable
            } else if (getBrcCsatWidgetRequestState is GetBrcCsatWidgetRequestState.Complete.Error) {
                getBrcCsatWidgetRequestState.throwable
            } else null
        }
    }
}
