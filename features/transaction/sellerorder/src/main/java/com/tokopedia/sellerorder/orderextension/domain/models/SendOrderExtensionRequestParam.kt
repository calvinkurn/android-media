package com.tokopedia.sellerorder.orderextension.domain.models

import com.google.gson.annotations.SerializedName

data class SendOrderExtensionRequestParam(
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("shop_id")
    val shopId: String,
    @SerializedName("reason_code")
    val reasonCode: Int,
    @SerializedName("reason_text")
    val reasonText: String
)
