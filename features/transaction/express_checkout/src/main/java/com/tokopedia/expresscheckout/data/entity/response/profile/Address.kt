package com.tokopedia.expresscheckout.data.entity.response.profile

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class Address(
        @SerializedName("address_id")
        val addressId: Int,

        @SerializedName("receiver_name")
        val receiverName: String,

        @SerializedName("address_name")
        val addressName: String,

        @SerializedName("address_street")
        val addressStreet: String,

        @SerializedName("district_id")
        val districtId: Int,

        @SerializedName("district_name")
        val districtName: String,

        @SerializedName("city_id")
        val cityId: Int,

        @SerializedName("city_name")
        val cityName: String,

        @SerializedName("province_id")
        val provinceId: Int,

        @SerializedName("province_name")
        val provinceName: String,

        @SerializedName("phone")
        val phone: String,

        @SerializedName("longitude")
        val longitude: String,

        @SerializedName("latitude")
        val latitude: String,

        @SerializedName("postal_code")
        val postalCode: String
)