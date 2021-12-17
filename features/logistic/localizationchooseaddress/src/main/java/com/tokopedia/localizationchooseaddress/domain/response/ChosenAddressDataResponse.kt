package com.tokopedia.localizationchooseaddress.domain.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ChosenAddressDataResponse (
        @SuppressLint("Invalid Data Type")
        @SerializedName("addr_id")
        var addressId: Long = 0,
        @SerializedName("receiver_name")
        var receiverName: String = "",
        @SerializedName("addr_name")
        var addressName: String = "",
        @SerializedName("district")
        var districtId: Int = 0,
        @SerializedName("city")
        var cityId: Int = 0,
        @SerializedName("city_name")
        var cityName: String = "",
        @SerializedName("district_name")
        var districtName: String = "",
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("latitude")
        var latitude: String = "",
        @SerializedName("longitude")
        var longitude: String = "",
        @SerializedName("postal_code")
        var postalCode: String = ""
)

data class Tokonow(
        @SuppressLint("Invalid Data Type")
        @SerializedName("shop_id")
        var shopId: Long = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("warehouse_id")
        var warehouseId: Long = 0
)

data class ErrorChosenAddress(
        @SerializedName("code")
        var code: Int = 0,
        @SerializedName("title")
        var title: String = ""
)
