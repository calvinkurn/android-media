package com.tokopedia.buyerorderdetail.domain.models

data class GetOrderResolutionParams(
    val orderId: Long,
    val shouldCheckCache: Boolean
)
