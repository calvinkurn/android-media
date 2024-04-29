package com.tokopedia.buyerorderdetail.domain.models

data class GetBrcCsatWidgetRequestParams(
    val orderId: Long,
    val shouldCheckCache: Boolean
)
