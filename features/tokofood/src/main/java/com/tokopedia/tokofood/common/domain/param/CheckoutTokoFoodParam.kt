package com.tokopedia.tokofood.common.domain.param

import com.google.gson.annotations.SerializedName

data class CheckoutTokoFoodParam(
    @SerializedName("additional_attributes")
    val additionalAttributes: String = "",
    @SerializedName("source")
    val source: String = ""
)
