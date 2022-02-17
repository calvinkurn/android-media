package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Warehouse {
    @SerializedName("WarehouseName")
    @Expose
    var warehouseName: String? = null

    @SerializedName("CityName")
    @Expose
    var cityName: String? = null
}