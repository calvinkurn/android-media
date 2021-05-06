package com.tokopedia.variant_common.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 08/03/20
 */

data class VariantMultiOriginResponse(
        @SerializedName("GetNearestWarehouse")
        @Expose
        val result: MultiOriginData = MultiOriginData()
)

data class VariantMultiOriginWarehouse(
        @SerializedName("product_id")
        @Expose
        val productId: String = "",

        @SerializedName("stock")
        @Expose
        val stock: Int = 0,

        @SerializedName("stock_wording")
        @Expose
        val stockWording: String = "",

        @SerializedName("price")
        @Expose
        val price: Int = 0,

        @SerializedName("warehouse_info")
        @Expose
        val warehouseInfo: WarehouseInfo = WarehouseInfo()
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

data class MultiOriginData(
        @SerializedName("data")
        @Expose
        val data: List<VariantMultiOriginWarehouse> = listOf()
)


