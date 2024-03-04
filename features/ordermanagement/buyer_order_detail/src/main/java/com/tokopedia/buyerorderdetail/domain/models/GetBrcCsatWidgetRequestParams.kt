package com.tokopedia.buyerorderdetail.domain.models

data class GetBrcCsatWidgetRequestParams(
    val orderID: String,
    val shouldCheckCache: Boolean
)
