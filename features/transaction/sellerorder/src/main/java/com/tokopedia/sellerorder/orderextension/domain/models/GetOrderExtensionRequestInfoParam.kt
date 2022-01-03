package com.tokopedia.sellerorder.orderextension.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetOrderExtensionRequestInfoParam(
    @SerializedName("order_id")
    @Expose
    val orderId: Long,
    @SerializedName("shop_id")
    @Expose
    val shopId: Long
)
