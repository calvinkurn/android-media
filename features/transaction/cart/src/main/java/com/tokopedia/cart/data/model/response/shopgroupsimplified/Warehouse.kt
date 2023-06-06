package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class Warehouse(
    @SerializedName("warehouse_id")
    var warehouseId: String = "",
    @SerializedName("district_id")
    var districtId: String = "",
    @SerializedName("postal_code")
    var postalCode: String = "",
    @SerializedName("longitude")
    var longitude: String = "",
    @SerializedName("latitude")
    var latitude: String = ""
)
