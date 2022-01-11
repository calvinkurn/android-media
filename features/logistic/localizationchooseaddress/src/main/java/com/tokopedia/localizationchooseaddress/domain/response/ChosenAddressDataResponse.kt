package com.tokopedia.localizationchooseaddress.domain.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChosenAddressDataResponse (
        @Expose
        @SuppressLint("Invalid Data Type")
        @SerializedName("addr_id")
        var addressId: Long = 0,

        @Expose
        @SerializedName("receiver_name")
        var receiverName: String = "",

        @Expose
        @SerializedName("addr_name")
        var addressName: String = "",

        @Expose
        @SerializedName("district")
        var districtId: Int = 0,

        @Expose
        @SerializedName("city")
        var cityId: Int = 0,

        @Expose
        @SerializedName("city_name")
        var cityName: String = "",

        @Expose
        @SerializedName("district_name")
        var districtName: String = "",

        @Expose
        @SerializedName("status")
        var status: Int = 0,

        @Expose
        @SerializedName("latitude")
        var latitude: String = "",

        @Expose
        @SerializedName("longitude")
        var longitude: String = "",

        @Expose
        @SerializedName("postal_code")
        var postalCode: String = ""
)

data class Tokonow(
        @Expose
        @SuppressLint("Invalid Data Type")
        @SerializedName("shop_id")
        var shopId: Long = 0,

        @Expose
        @SuppressLint("Invalid Data Type")
        @SerializedName("warehouse_id")
        var warehouseId: Long = 0,

        @Expose
        @SerializedName("warehouses")
        var warehouses: List<Warehouse> = listOf(),

        @Expose
        @SerializedName("service_type")
        var serviceType: String = ""
)

data class ErrorChosenAddress(
        @Expose
        @SerializedName("code")
        var code: Int = 0,

        @Expose
        @SerializedName("title")
        var title: String = ""
)

data class Warehouse(
        @Expose
        @SuppressLint("Invalid Data Type")
        @SerializedName("warehouse_id")
        var warehouseId: Long = 0,

        @Expose
        @SerializedName("service_type")
        var serviceType: String = ""
)
