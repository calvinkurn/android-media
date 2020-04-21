package com.tokopedia.variant_common.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 16/04/20
 */
data class WarehouseInfoVariant(
        @SerializedName("warehouseID")
        @Expose
        val warehouseId: String = "",

        @SerializedName("isFulfillment")
        @Expose
        val isFulfillment: Boolean = false,

        @SerializedName("districtID")
        @Expose
        val districtId: String = "",

        @SerializedName("postalCode")
        @Expose
        val postalCode: String = "",

        @SerializedName("geoLocation")
        @Expose
        val geoLocation: String = ""
)