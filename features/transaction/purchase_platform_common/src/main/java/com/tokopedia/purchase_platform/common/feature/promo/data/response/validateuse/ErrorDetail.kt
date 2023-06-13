package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import com.google.gson.annotations.SerializedName

data class ErrorDetail(

    @field:SerializedName("message")
    val message: String = ""
)
