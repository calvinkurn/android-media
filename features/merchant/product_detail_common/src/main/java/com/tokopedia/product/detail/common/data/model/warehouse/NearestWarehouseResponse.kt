package com.tokopedia.product.detail.common.data.model.warehouse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 28/07/20
 */
data class NearestWarehouseResponse(
        @SerializedName("product_id")
        @Expose
        var productId: String = "",

        @SerializedName("warehouse_info")
        @Expose
        var warehouseInfo: WarehouseInfo = WarehouseInfo()
)

data class WarehouseInfo(
        @SerializedName("warehouse_id")
        @Expose
        val id: String = "",

        @SerializedName("is_fulfillment")
        @Expose
        val isFulfillment: Boolean = false,

        @SerializedName("district_id")
        @Expose
        val districtId: String = "",

        @SerializedName("postal_code")
        @Expose
        val postalCode: String = "",

        @SerializedName("geolocation")
        @Expose
        val geoLocation: String = ""
) {
    fun getOrigin(): String? {
        return if (districtId.isNotBlank() && (postalCode.isNotBlank() || geoLocation.isNotBlank())) {
            arrayOf(districtId, postalCode, geoLocation).joinToString("|")
        } else null
    }
}