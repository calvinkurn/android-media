package com.tokopedia.shop.info.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetNearestEpharmacyWarehouseLocationResponse(
    @SerializedName("getNearestEpharmacyWarehouseLocation")
    @Expose
    val getNearestEpharmacyWarehouseLocation: NearestEpharmacyData = NearestEpharmacyData()
) {

    data class NearestEpharmacyData(
        @SerializedName("data")
        @Expose
        val data: GetNearestEpharmacyWarehouseLocationDetailData = GetNearestEpharmacyWarehouseLocationDetailData()
    ) {
        data class GetNearestEpharmacyWarehouseLocationDetailData(
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
}
