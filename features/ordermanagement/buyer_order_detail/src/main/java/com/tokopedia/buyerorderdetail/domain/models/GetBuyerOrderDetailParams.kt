package com.tokopedia.buyerorderdetail.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetBuyerOrderDetailParams(
    @Expose
    @SerializedName("cart_string")
    val cart: String = "",
    @Expose
    @SerializedName("order_id")
    val orderId: String = "0",
    @Expose
    @SerializedName("payment_id")
    val paymentId: String = "",
    @Expose(serialize = false, deserialize = false)
    @Transient
    val shouldCheckCache: Boolean
)
