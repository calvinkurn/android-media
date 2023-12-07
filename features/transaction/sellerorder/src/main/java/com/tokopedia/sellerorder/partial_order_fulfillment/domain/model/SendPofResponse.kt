package com.tokopedia.sellerorder.partial_order_fulfillment.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SendPofResponse(
    @SerializedName("data")
    @Expose
    val `data`: Data? = null
) {
    data class Data(
        @SerializedName("request_partial_order_fulfillment")
        @Expose
        val requestPartialOrderFulfillment: RequestPartialOrderFulfillment? = null
    ) {
        data class RequestPartialOrderFulfillment(
            @SerializedName("success")
            @Expose
            val success: Int? = null
        )
    }
}
