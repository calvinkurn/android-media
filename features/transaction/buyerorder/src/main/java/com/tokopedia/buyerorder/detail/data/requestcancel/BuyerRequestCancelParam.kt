package com.tokopedia.buyerorder.detail.data.requestcancel


import com.google.gson.annotations.SerializedName

data class BuyerRequestCancelParam(
    @SerializedName("user_id")
    val userId: String = "",

    @SerializedName("order_id")
    val orderId: String = "",

    @SerializedName("reason")
    val reason: String = "",

    @SerializedName("lang")
    val lang: String = "id",

    @SerializedName("reason_code")
    val reasonCode: String = "",

    @SerializedName("reason_cancel")
    val reasonCancel: String = "",

    @SerializedName("os_type")
    val osType: String = ""
)