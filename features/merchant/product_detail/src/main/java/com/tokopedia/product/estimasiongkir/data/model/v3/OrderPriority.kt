package com.tokopedia.product.estimasiongkir.data.model.v3

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderPriority(
        @SerializedName("formatted_price")
        @Expose
        val priceFmt: String = "",

        @SerializedName("inactive_message")
        @Expose
        val inactiveMessage: String = "",

        @SerializedName("is_now")
        @Expose
        val isNow: Boolean = false,

        @SerializedName("price")
        @Expose
        val price: Int = 0
)