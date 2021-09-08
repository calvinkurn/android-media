package com.tokopedia.checkout.bundle.data.model.response.checkout

import com.google.gson.annotations.SerializedName

data class CheckoutGqlResponse(
        @SerializedName("checkout")
        val checkout: CheckoutResponse = CheckoutResponse()
)