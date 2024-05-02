package com.tokopedia.minicart.common.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WarehouseData(
    @Expose
    @SerializedName("warehouseID")
    val warehouseId: String,
    @Expose
    @SerializedName("serviceType")
    val serviceType: String
)
