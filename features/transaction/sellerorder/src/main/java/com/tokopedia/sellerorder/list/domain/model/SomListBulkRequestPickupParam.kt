package com.tokopedia.sellerorder.list.domain.model

import com.google.gson.annotations.SerializedName

data class SomListBulkRequestPickupParam(
        @SerializedName("data")
        val data: List<BulkRequestPickupInputData> = emptyList()
) {
    data class BulkRequestPickupInputData(
            @SerializedName("order_id")
            val orderId: String = ""
    )
}