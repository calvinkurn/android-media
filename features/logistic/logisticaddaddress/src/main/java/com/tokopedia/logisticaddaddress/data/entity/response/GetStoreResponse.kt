package com.tokopedia.logisticaddaddress.data.entity.response

import com.google.gson.annotations.SerializedName

data class GetStoreResponse(
        @SerializedName("keroAddressStoreLocation")
        var keroAddressStoreLocation: KeroAddressStoreLocation = KeroAddressStoreLocation()
)

data class KeroAddressStoreLocation(
        @SerializedName("config")
        var config: String = "",
        @SerializedName("data")
        var `data`: List<Data> = listOf(),
        @SerializedName("global_radius")
        var globalRadius: Int = 0,
        @SerializedName("server_process_time")
        var serverProcessTime: String = "",
        @SerializedName("status")
        var status: String = ""
)

data class Data(
        @SerializedName("addr_id")
        var addrId: Int = 0,
        @SerializedName("addr_name")
        var addrName: String = "",
        @SerializedName("address_1")
        var address1: String = "",
        @SerializedName("address_2")
        var address2: String = "",
        @SerializedName("city")
        var city: Int = 0,
        @SerializedName("city_name")
        var cityName: String = "",
        @SerializedName("country")
        var country: String = "",
        @SerializedName("district")
        var district: Int = 0,
        @SerializedName("district_name")
        var districtName: String = "",
        @SerializedName("latitude")
        var latitude: String = "",
        @SerializedName("longitude")
        var longitude: String = "",
        @SerializedName("opening_hours")
        var openingHours: String = "",
        @SerializedName("phone")
        var phone: String = "",
        @SerializedName("postal_code")
        var postalCode: String = "",
        @SerializedName("province")
        var province: Int = 0,
        @SerializedName("province_name")
        var provinceName: String = "",
        @SerializedName("receiver_name")
        var receiverName: String = "",
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("store_code")
        var storeCode: String = "",
        @SerializedName("store_distance")
        var storeDistance: String = "",
        @SerializedName("type")
        var type: Int = 0
)