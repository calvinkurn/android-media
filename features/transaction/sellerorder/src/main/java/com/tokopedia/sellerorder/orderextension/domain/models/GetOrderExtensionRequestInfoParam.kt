package com.tokopedia.sellerorder.orderextension.domain.models

import com.google.gson.annotations.SerializedName

data class GetOrderExtensionRequestInfoParam(
    @SerializedName("order_id")
    val orderId: Long,
    @SerializedName("shop_id")
    val shopId: Long
)
