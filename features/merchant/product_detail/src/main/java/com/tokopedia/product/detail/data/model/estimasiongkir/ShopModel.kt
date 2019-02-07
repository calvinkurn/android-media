package com.tokopedia.product.detail.data.model.estimasiongkir

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopModel(
        @SerializedName("district_name")
        @Expose
        val districtName: String = "",
        @SerializedName("postal_code")
        @Expose
        val postalCode: String = "",
        @SerializedName("origin")
        @Expose
        val origin: String = "",
        @SerializedName("addr_street")
        @Expose
        val addrStreet: String = "",
        @SerializedName("latitude")
        @Expose
        val latitude: String = "",
        @SerializedName("longitude")
        @Expose
        val longitude: String = "",
        @SerializedName("city_name")
        @Expose
        val cityName: String = ""
)