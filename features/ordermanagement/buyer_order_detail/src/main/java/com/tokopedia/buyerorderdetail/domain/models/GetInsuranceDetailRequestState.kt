package com.tokopedia.buyerorderdetail.domain.models

import java.io.Serializable

sealed interface GetInsuranceDetailRequestState : Serializable {
    object Requesting : GetInsuranceDetailRequestState
    sealed interface Complete : GetInsuranceDetailRequestState {
        data class Success(
            val result: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data?
        ) : Complete

        data class Error(val throwable: Throwable?) : Complete
    }
}

