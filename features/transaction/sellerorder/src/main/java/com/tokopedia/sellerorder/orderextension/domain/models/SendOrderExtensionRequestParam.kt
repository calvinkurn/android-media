package com.tokopedia.sellerorder.orderextension.domain.models

import com.google.gson.annotations.SerializedName

data class SendOrderExtensionRequestParam(
    @SerializedName("order_id")
    val orderId: Long,
    @SerializedName("shop_id")
    val shopId: Long,
    @SerializedName("reason_code")
    val reasonCode: Int,
    @SerializedName("reason_text")
    val reasonText: String
)
