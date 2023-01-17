package com.tokopedia.buyerorderdetail.domain.models


import com.google.gson.annotations.SerializedName

data class RejectPartialOrderFulfillmentResponse(
    @SerializedName("reject_partial_order_fulfillment")
    val rejectPartialOrderFulfillment: RejectPartialOrderFulfillment = RejectPartialOrderFulfillment()
)

data class RejectPartialOrderFulfillment(
    @SerializedName("success")
    val success: Int = 0
)
