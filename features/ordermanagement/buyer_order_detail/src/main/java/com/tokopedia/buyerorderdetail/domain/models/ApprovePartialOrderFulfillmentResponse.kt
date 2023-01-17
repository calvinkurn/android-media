package com.tokopedia.buyerorderdetail.domain.models


import com.google.gson.annotations.SerializedName

data class ApprovePartialOrderFulfillmentResponse(
    @SerializedName("approve_partial_order_fulfillment")
    val approvePartialOrderFulfillment: ApprovePartialOrderFulfillment = ApprovePartialOrderFulfillment()
)

data class ApprovePartialOrderFulfillment(
    @SerializedName("success")
    val success: Int = 0
)
