package com.tokopedia.product.estimasiongkir.data.model.v3

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddressModel(
        @SerializedName("city_name")
        @Expose
        val cityName: String = "",
        @SerializedName("province_name")
        @Expose
        val provinceName: String = "",
        @SerializedName("district_name")
        @Expose
        val districtName: String = "",
        @SerializedName("country")
        @Expose
        val country: String = "",
        @SerializedName("postal_code")
        @Expose
        val postalCode: String = "",
        @SerializedName("address")
        @Expose
        val address: String = "",
        @SerializedName("phone")
        @Expose
        val phone: String = "",
        @SerializedName("lat")
        @Expose
        val lat: String = "",
        @SerializedName("long")
        @Expose
        val long: String = "",
        @SerializedName("addr_name")
        @Expose
        val addrName: String = "",
        @SerializedName("address_1")
        @Expose
        val address1: String = "",
        @SerializedName("receiver_name")
        @Expose
        val receiverName: String = ""
)