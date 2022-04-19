package com.tokopedia.oneclickcheckout.order.data.get

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

class WarehouseDataResponse(
        @SuppressLint("Invalid Data Type")
        @SerializedName("warehouse_id")
        val warehouseId: Long = 0,
        @SerializedName("is_fulfillment")
        val isFulfillment: Boolean = false
)