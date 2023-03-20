package com.tokopedia.checkout.data.model.response.checkout

import com.google.gson.annotations.SerializedName

data class CheckoutGqlResponse(
    @SerializedName("checkout")
    val checkout: CheckoutResponse = CheckoutResponse()
)
