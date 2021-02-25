package com.tokopedia.oneclickcheckout.common.data.model

import com.google.gson.annotations.SerializedName

data class Address(
        @SerializedName("address_id")
        val addressId: Int = 0,
        @SerializedName("receiver_name")
        val receiverName: String = "",
        @SerializedName("address_name")
        val addressName: String = "",
        @SerializedName("address_street")
        val addressStreet: String = "",
        @SerializedName("district_id")
        val districtId: Int = 0,
        @SerializedName("district_name")
        val districtName: String = "",
        @SerializedName("city_id")
        val cityId: Int = 0,
        @SerializedName("city_name")
        val cityName: String = "",
        @SerializedName("province_id")
        val provinceId: Int = 0,
        @SerializedName("province_name")
        val provinceName: String = "",
        @SerializedName("phone")
        val phone: String = "",
        @SerializedName("longitude")
        val longitude: String = "",
        @SerializedName("latitude")
        val latitude: String = "",
        @SerializedName("postal_code")
        val postalCode: String = "",
        @SerializedName("state")
        val state: Int = 0,
        @SerializedName("state_detail")
        val stateDetail: String = ""
)
