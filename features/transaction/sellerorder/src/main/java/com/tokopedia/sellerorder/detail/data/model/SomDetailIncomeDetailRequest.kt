package com.tokopedia.sellerorder.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomDetailIncomeDetailRequest(
    @SerializedName("order_id")
    @Expose
    val orderId: Long
)
