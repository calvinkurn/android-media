package com.tokopedia.buyerorderdetail.domain.models

import java.io.Serializable

sealed class GetBuyerOrderDetailRequestState : Serializable {

    abstract val type: String

    object Requesting : GetBuyerOrderDetailRequestState() {
        override val type: String = Requesting::class.java.simpleName
    }

    data class Success(
        val result: GetBuyerOrderDetailResponse.Data.BuyerOrderDetail
    ) : GetBuyerOrderDetailRequestState() {
        override val type: String = Success::class.java.simpleName
    }

    data class Error(val throwable: Throwable) : GetBuyerOrderDetailRequestState() {
        override val type: String = Error::class.java.simpleName
    }
}
