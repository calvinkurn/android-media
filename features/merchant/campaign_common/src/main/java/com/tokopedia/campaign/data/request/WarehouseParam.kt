package com.tokopedia.campaign.data.request

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class WarehouseParam(
    @SuppressLint("Invalid Data Type")
    @SerializedName("warehouse_id")
    val warehouseId: Long = 0,
    @SerializedName("service_type")
    val serviceType: String = ""
)
