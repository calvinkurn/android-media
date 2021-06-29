package com.tokopedia.sellerorder.list.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListBulkConfirmShippingParam(
        @Expose
        @SerializedName("data")
        val data: List<BulkConfirmShippingInputData> = emptyList()
) {
    data class BulkConfirmShippingInputData(
            @Expose
            @SerializedName("order_id")
            val orderId: String = "",
            @Expose
            @SerializedName("shipping_ref")
            val shippingRef: String = ""
    )
}