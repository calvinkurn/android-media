package com.tokopedia.buyerorderdetail.domain.models

sealed interface GetOrderResolutionRequestState {
    object Requesting : GetOrderResolutionRequestState
    sealed interface Complete : GetOrderResolutionRequestState {

        data class Success(
            val result: GetOrderResolutionResponse.ResolutionGetTicketStatus.ResolutionData?
        ) : Complete

        data class Error(val throwable: Throwable?) : Complete
    }
}
