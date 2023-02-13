package com.tokopedia.sellerorder.orderextension.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SendOrderExtensionRequestParam(
    @SerializedName("order_id")
    @Expose
    val orderId: Long,
    @SerializedName("shop_id")
    @Expose
    val shopId: Long,
    @SerializedName("reason_code")
    @Expose
    val reasonCode: Int,
    @SerializedName("reason_text")
    @Expose
    val reasonText: String,
    @SerializedName("extension_time")
    @Expose
    val extensionTime: Int
)
