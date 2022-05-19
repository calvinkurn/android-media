package com.tokopedia.ordermanagement.buyercancellationorder.data.requestcancel


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BuyerRequestCancelParam(
    @Expose
    @SerializedName("user_id")
    val userId: String = "",

    @Expose
    @SerializedName("order_id")
    val orderId: String = "",

    @Expose
    @SerializedName("reason")
    val reason: String = "",

    @Expose
    @SerializedName("lang")
    val lang: String = "id",

    @Expose
    @SerializedName("reason_code")
    val reasonCode: String = "",

    @Expose
    @SerializedName("reason_cancel")
    val reasonCancel: String = "",

    @Expose
    @SerializedName("os_type")
    val osType: String = ""
)