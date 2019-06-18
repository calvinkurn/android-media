package com.tokopedia.product.detail.common.data.model.warehouse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
)