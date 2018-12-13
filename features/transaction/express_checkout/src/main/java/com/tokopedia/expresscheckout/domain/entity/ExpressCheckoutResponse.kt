package com.tokopedia.expresscheckout.domain.entity

import com.google.gson.annotations.SerializedName

data class ExpressCheckoutResponse(
        @SerializedName("header")
        val header: ExpressCheckoutFormHeader,

        @SerializedName("data")
        val data: ExpressCheckoutFormData,

        @SerializedName("status")
        val status: String
)