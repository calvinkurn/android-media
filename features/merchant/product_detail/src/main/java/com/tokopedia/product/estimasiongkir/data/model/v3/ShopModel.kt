package com.tokopedia.product.estimasiongkir.data.model.v3

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopModel(
        @SerializedName("district_name")
        @Expose
        val districtName: String = "",

        @SerializedName("city_name")
        @Expose
        val cityName: String = ""
)