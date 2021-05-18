package com.tokopedia.checkout.data.model.response.shipmentaddressform

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class Warehouse(
        @SuppressLint("Invalid Data Type")
        @SerializedName("warehouse_id")
        val warehouseId: Long = 0,
        @SerializedName("city_name")
        val cityName: String = ""
)