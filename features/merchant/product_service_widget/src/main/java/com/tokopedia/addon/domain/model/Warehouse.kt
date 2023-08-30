package com.tokopedia.addon.domain.model

import com.google.gson.annotations.SerializedName

data class Warehouse (
    @SerializedName("WarehouseName")
    var warehouseName: String = "",

    @SerializedName("CityName")
    var cityName: String = ""
)
