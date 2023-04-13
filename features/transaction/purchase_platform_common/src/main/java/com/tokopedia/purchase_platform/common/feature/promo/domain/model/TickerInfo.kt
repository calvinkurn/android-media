package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class TickerInfo(
    @field:SerializedName("unique_id")
    val uniqueId: String = "",
    @field:SerializedName("status_code")
    val statusCode: Int = 0,
    @field:SerializedName("message")
    val message: String = ""
)
