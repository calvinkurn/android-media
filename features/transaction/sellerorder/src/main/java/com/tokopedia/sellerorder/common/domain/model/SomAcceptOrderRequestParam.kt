package com.tokopedia.sellerorder.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomAcceptOrderRequestParam (
        @SerializedName("order_id")
        @Expose
        val orderId: String = "0",
        @SerializedName("shop_id")
        @Expose
        val shopId: String = "0",
        @SerializedName("is_from_fintech")
        @Expose
        val isFromFintech: Boolean = false
)