package com.tokopedia.logisticCommon.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class AddAddressResponse (
        @SerializedName("kero_add_address")
        val keroAddAddress: KeroAddAddress = KeroAddAddress()
)

data class KeroAddAddress(
        @SerializedName("data")
        val data: DataAddAddress = DataAddAddress(),

        @SerializedName("server_process_time")
        val serverProcessTime: String = "",

        @SerializedName("config")
        val config: String = "",

        @SerializedName("status")
        val status: String = ""
)

data class DataAddAddress(
        @SerializedName("is_success")
        val isSuccess: Int = 0,

        @SuppressLint("Invalid Data Type")
        @SerializedName("addr_id")
        val addrId: Long = 0,

        @SerializedName("is_state_chosen_address_change")
        val isStateChosenAddressChange: Boolean = true,

        @SerializedName("chosen_address")
        val chosenAddress: ChosenAddressAddResponse = ChosenAddressAddResponse(),

        @SerializedName("tokonow")
        val tokonow: TokonowAddAddress = TokonowAddAddress()
)

data class ChosenAddressAddResponse(
        @SuppressLint("Invalid Data Type")
        @SerializedName("addr_id")
        var addressId: Int = 0,
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

@SuppressLint("Invalid Data Type")
data class TokonowAddAddress(
        @SerializedName("shop_id")
        val shopId: Long = 0,
        @SerializedName("warehouse_id")
        val warehouseId: Long = 0
)