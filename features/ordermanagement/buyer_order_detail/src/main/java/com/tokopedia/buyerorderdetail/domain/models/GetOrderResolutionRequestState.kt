package com.tokopedia.buyerorderdetail.domain.models

import java.io.Serializable

sealed class GetOrderResolutionRequestState : Serializable {

    abstract val type: String

    object Requesting : GetOrderResolutionRequestState() {
        override val type: String = Requesting::class.java.simpleName
    }

    data class Success(
        val result: GetResolutionTicketStatusResponse.ResolutionGetTicketStatus.ResolutionData?
    ) : GetOrderResolutionRequestState() {
        override val type: String = Success::class.java.simpleName
    }

    data class Error(val throwable: Throwable) : GetOrderResolutionRequestState() {
        override val type: String = Error::class.java.simpleName
    }
}
