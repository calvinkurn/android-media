package com.tokopedia.checkout.domain.datamodel.newaddresscorner

import com.google.gson.annotations.SerializedName

data class NewAddressCornerResponse(
        @SerializedName("keroAddressCorner")
        val keroAddressCorner: KeroAddressCorner
)

data class KeroAddressCorner(
        @SerializedName("data")
        val `data`: List<Data>,
        @SerializedName("config")
        val config: String,
        @SerializedName("server_process_time")
        val serverProcessTime: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("token")
        val token: Token
)

data class Data(
        @SerializedName("addr_id")
        val addrId: Int,
        @SerializedName("addr_name")
        val addrName: String,
        @SerializedName("address_1")
        val address1: String,
        @SerializedName("address_2")
        val address2: String,
        @SerializedName("city")
        val city: Int,
        @SerializedName("city_name")
        val cityName: String,
        @SerializedName("country")
        val country: String,
        @SerializedName("district")
        val district: Int,
        @SerializedName("district_name")
        val districtName: String,
        @SerializedName("is_active")
        val isActive: Boolean,
        @SerializedName("is_corner")
        val isCorner: Boolean,
        @SerializedName("is_primary")
        val isPrimary: Boolean,
        @SerializedName("is_whitelist")
        val isWhitelist: Boolean,
        @SerializedName("latitude")
        val latitude: String,
        @SerializedName("longitude")
        val longitude: String,
        @SerializedName("partner_id")
        val partnerId: Int,
        @SerializedName("partner_name")
        val partnerName: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("postal_code")
        val postalCode: String,
        @SerializedName("province")
        val province: Int,
        @SerializedName("province_name")
        val provinceName: String,
        @SerializedName("receiver_name")
        val receiverName: String,
        @SerializedName("status")
        val status: Int,
        @SerializedName("type")
        val type: Int
)

data class Token(
        @SerializedName("district_recommentdation")
        val districtRecommendation: String,
        @SerializedName("ut")
        val ut: String
)