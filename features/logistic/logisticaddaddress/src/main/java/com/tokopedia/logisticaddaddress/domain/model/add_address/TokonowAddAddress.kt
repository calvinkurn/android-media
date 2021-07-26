package com.tokopedia.logisticaddaddress.domain.model.add_address

import com.google.gson.annotations.SerializedName

data class TokonowAddAddress(
        @SerializedName("shop_id")
        val shopId: Long = 0,
        @SerializedName("warehouse_id")
        val warehouseId: Long = 0
)