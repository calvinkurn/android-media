package com.tokopedia.logisticCommon.data.response

import com.google.gson.annotations.SerializedName

data class AddressResponse(
        @SerializedName("keroAddressCorner")
        var keroAddressCorner: KeroAddressCorner = KeroAddressCorner()
)

data class KeroAddressCorner(
        @SerializedName("data")
        var `data`: List<DataAddress> = listOf(),
        @SerializedName("config")
        var config: String = "",
        @SerializedName("server_process_time")
        var serverProcessTime: String = "",
        @SerializedName("status")
        var status: String = "",
        @SerializedName("token")
        var token: Token = Token(),
        @SerializedName("has_next")
        var hasNext: Boolean = false
)

data class Token(
        @SerializedName("district_recommendation")
        var districtRecommendation: String = "",
        @SerializedName("ut")
        var ut: String = ""
)

data class DataAddress(
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
        @SerializedName("is_active")
        var isActive: Boolean = false,
        @SerializedName("is_corner")
        var isCorner: Boolean = false,
        @SerializedName("is_primary")
        var isPrimary: Boolean = false,
        @SerializedName("is_whitelist")
        var isWhitelist: Boolean = false,
        @SerializedName("latitude")
        var latitude: String = "",
        @SerializedName("longitude")
        var longitude: String = "",
        @SerializedName("partner_id")
        var partnerId: Int = 0,
        @SerializedName("partner_name")
        var partnerName: String = "",
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
        @SerializedName("type")
        var type: Int = 0
)