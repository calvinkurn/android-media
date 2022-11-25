package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class WarehouseDataResponse(
        @SerializedName("warehouse_id")
        val warehouseId: String = "",
        @SerializedName("is_fulfillment")
        val isFulfillment: Boolean = false
)
