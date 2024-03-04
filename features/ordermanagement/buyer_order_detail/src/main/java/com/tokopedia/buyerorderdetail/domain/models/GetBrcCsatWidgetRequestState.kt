package com.tokopedia.buyerorderdetail.domain.models

interface GetBrcCsatWidgetRequestState {
    object Requesting : GetBrcCsatWidgetRequestState
    sealed interface Complete : GetBrcCsatWidgetRequestState {
        data class Success(val result: GetBrcCsatWidgetResponse.ResolutionGetCsatFormV4?) : Complete
        data class Error(val throwable: Throwable?) : Complete
    }
}
