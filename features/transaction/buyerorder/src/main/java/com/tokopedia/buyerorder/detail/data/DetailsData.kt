package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class DetailsData(
    @SerializedName("orderDetails")
    @Expose
    val orderDetails: OrderDetails = OrderDetails()
)