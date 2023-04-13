package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class ErrorDetail(
    @field:SerializedName("message")
    val message: String = ""
)
