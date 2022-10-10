package com.tokopedia.buyerorderdetail.domain.models

import java.io.Serializable

sealed class GetInsuranceDetailRequestState : Serializable {

    abstract val type: String

    object Requesting : GetInsuranceDetailRequestState() {
        override val type: String = Requesting::class.java.simpleName
    }

    data class Success(
        val result: GetInsuranceDetailResponse.Data.PpGetInsuranceDetail.Data?
    ) : GetInsuranceDetailRequestState() {
        override val type: String = Success::class.java.simpleName
    }

    data class Error(val throwable: Throwable) : GetInsuranceDetailRequestState() {
        override val type: String = Error::class.java.simpleName
    }
}

