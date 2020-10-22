package com.tokopedia.sellerorder.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomEditRefNumRequestParam(
        @SerializedName("order_id")
        @Expose
        val orderId: String = "0",
        @SerializedName("shipping_ref")
        @Expose
        val shippingRef: String = ""
)