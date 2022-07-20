package com.tokopedia.checkout.data.model.response.checkout

import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("redirect_url")
        val redirectUrl: String = "",
        @SerializedName("callback_url")
        val callbackUrl: String = "",
        @SerializedName("parameter")
        val parameter: Parameter = Parameter(),
        @SerializedName("query_string")
        val queryString: String = "",
        @SerializedName("price_validation")
        val priceValidation: PriceValidation = PriceValidation()
)