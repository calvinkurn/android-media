package com.tokopedia.buyerorderdetail.domain.models

data class GetP1DataParams(
    val hasResoStatus: Boolean,
    val hasInsurance: Boolean,
    val orderId: Long,
    val invoice: String,
    val shouldCheckCache: Boolean
)
