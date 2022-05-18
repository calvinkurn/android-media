package com.tokopedia.ordermanagement.buyercancellationorder.data.instantcancellation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class BuyerInstantCancelParam(
    @Expose
    @SerializedName("order_id")
    val orderId: String = "",

    @Expose
    @SerializedName("lang")
    val lang: String = "id",

    @Expose
    @SerializedName("reason_code")
    val reasonCode: String = "",

    @Expose
    @SerializedName("reason")
    val reason: String = ""
)