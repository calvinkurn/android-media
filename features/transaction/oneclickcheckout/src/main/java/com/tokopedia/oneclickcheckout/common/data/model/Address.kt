package com.tokopedia.oneclickcheckout.common.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class Address(
        @SuppressLint("Invalid Data Type")
        @SerializedName("address_id")
        val addressId: Long = 0,
        @SerializedName("receiver_name")
        val receiverName: String = "",
        @SerializedName("address_name")
        val addressName: String = "",
        @SerializedName("address_street")
        val addressStreet: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("district_id")
        val districtId: Long = 0,
        @SerializedName("district_name")
        val districtName: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("city_id")
        val cityId: Long = 0,
        @SerializedName("city_name")
        val cityName: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("province_id")
        val provinceId: Long = 0,
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
        val stateDetail: String = "",
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("tokonow")
        val tokoNow: AddressTokoNow = AddressTokoNow()
)

data class AddressTokoNow(
        @SerializedName("shop_id")
        val shopId: String = "",
        @SerializedName("warehouse_id")
        val warehouseId: String = ""
)
