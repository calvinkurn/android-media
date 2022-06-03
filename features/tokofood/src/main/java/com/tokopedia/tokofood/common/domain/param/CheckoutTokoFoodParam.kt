package com.tokopedia.tokofood.common.domain.param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckoutTokoFoodParam(
    @SerializedName("additional_attributes")
    @Expose
    val additionalAttributes: String = "",
    @SerializedName("source")
    @Expose
    val source: String = ""
)