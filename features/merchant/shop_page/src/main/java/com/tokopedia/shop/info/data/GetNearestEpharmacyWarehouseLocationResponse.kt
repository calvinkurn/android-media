package com.tokopedia.shop.info.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetNearestEpharmacyWarehouseLocationResponse(
    @SerializedName("getNearestEpharmacyWarehouseLocation")
    @Expose
    val getNearestEpharmacyWarehouseLocation: GetNearestEpharmacyWarehouseLocationData = GetNearestEpharmacyWarehouseLocationData()
) {

    data class GetNearestEpharmacyWarehouseLocationData(
        @SerializedName("gMapsURL")
        @Expose
        val gMapsURL: String = "",
        @SerializedName("address")
        @Expose
        val address: String = "",
        @SerializedName("warehouseID")
        @Expose
        val warehouseID: Long = 0L
    )
}
