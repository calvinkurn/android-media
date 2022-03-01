package com.tokopedia.logisticCommon.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddAddressResponse (
        @Expose
        @SerializedName("kero_add_address")
        val keroAddAddress: KeroAddAddress = KeroAddAddress()
)

data class KeroAddAddress(
        @Expose
        @SerializedName("data")
        val data: DataAddAddress = DataAddAddress(),

        @Expose
        @SerializedName("server_process_time")
        val serverProcessTime: String = "",

        @Expose
        @SerializedName("config")
        val config: String = "",

        @Expose
        @SerializedName("status")
        val status: String = ""
)

data class DataAddAddress(
        @Expose
        @SerializedName("is_success")
        val isSuccess: Int = 0,

        @Expose
        @SuppressLint("Invalid Data Type")
        @SerializedName("addr_id")
        val addrId: Long = 0,

        @Expose
        @SerializedName("is_state_chosen_address_change")
        val isStateChosenAddressChange: Boolean = true,

        @Expose
        @SerializedName("chosen_address")
        val chosenAddress: ChosenAddressAddResponse = ChosenAddressAddResponse(),

        @Expose
        @SerializedName("tokonow")
        val tokonow: TokonowAddAddress = TokonowAddAddress()
)

data class ChosenAddressAddResponse(
        @Expose
        @SuppressLint("Invalid Data Type")
        @SerializedName("addr_id")
        var addressId: Int = 0,

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

@SuppressLint("Invalid Data Type")
data class TokonowAddAddress(
        @Expose
        @SerializedName("shop_id")
        val shopId: Long = 0,

        @Expose
        @SerializedName("warehouse_id")
        val warehouseId: Long = 0,

        @Expose
        @SerializedName("warehouses")
        val warehouses: List<WarehousesAddAddress> = listOf(),

        @Expose
        @SerializedName("service_type")
        val serviceType: String = ""
)

data class WarehousesAddAddress(
        @Expose
        @SuppressLint("Invalid Data Type")
        @SerializedName("warehouse_id")
        val warehouseId: Long = 0,

        @Expose
        @SerializedName("service_type")
        val serviceType: String = ""
)