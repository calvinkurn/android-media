package com.tokopedia.ordermanagement.buyercancellationorder.data.getcancellationreason

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BuyerGetCancellationReasonParam(

    @SerializedName("user_id")
    @Expose
    val userId: String = "",

    @Expose
    @SerializedName("order_id")
    val orderId: String = ""
)