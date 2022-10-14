package com.tokopedia.buyerorderdetail.domain.models

sealed class GetOrderResolutionRequestState {

    object Requesting : GetOrderResolutionRequestState()

    data class Success(
        val result: GetOrderResolutionResponse.ResolutionGetTicketStatus.ResolutionData?
    ) : GetOrderResolutionRequestState()

    data class Error(val throwable: Throwable?) : GetOrderResolutionRequestState()
}
