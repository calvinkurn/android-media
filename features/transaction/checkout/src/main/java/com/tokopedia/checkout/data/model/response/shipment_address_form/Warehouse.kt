package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName

data class Warehouse(
        @SerializedName("warehouse_id")
        val warehouseId: Int = 0,
        @SerializedName("city_name")
        val cityName: String = ""
)