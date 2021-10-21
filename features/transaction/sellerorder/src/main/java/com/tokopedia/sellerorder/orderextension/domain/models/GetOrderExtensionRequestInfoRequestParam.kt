package com.tokopedia.sellerorder.orderextension.domain.models

import com.google.gson.annotations.SerializedName

data class GetOrderExtensionRequestInfoRequestParam(
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("shop_id")
    val shopId: String
)
