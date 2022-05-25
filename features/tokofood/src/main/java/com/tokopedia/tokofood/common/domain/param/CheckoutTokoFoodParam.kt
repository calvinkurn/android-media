package com.tokopedia.tokofood.common.domain.param

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckoutTokoFoodParam(
    @SerializedName("additional_attributes")
    @Expose
    val additionalAttributes: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("source")
    @Expose
    val source: String = ""
)