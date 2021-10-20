package com.tokopedia.checkout.old.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class UserAddress(
        @SerializedName("address_id")
        val addressId: String = "",
        @SerializedName("address_name")
        val addressName: String = "",
        @SerializedName("address")
        val address: String = "",
        @SerializedName("postal_code")
        val postalCode: String = "",
        @SerializedName("phone")
        val phone: String = "",
        @SerializedName("receiver_name")
        val receiverName: String = "",
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("country")
        val country: String = "",
        @SerializedName("province_id")
        val provinceId: String = "",
        @SerializedName("province_name")
        val provinceName: String = "",
        @SerializedName("city_id")
        val cityId: String = "",
        @SerializedName("city_name")
        val cityName: String = "",
        @SerializedName("district_id")
        val districtId: String = "",
        @SerializedName("district_name")
        val districtName: String = "",
        @SerializedName("address_2")
        val address2: String = "",
        @SerializedName("latitude")
        val latitude: String = "",
        @SerializedName("longitude")
        val longitude: String = "",
        @SerializedName("corner_id")
        val cornerId: String = "",
        @SerializedName("is_corner")
        val isCorner: Boolean = false,
        @SerializedName("state")
        val state: Int = 0,
        @SerializedName("state_detail")
        val stateDetail: String = "",
        @SerializedName("tokonow")
        val tokoNow: UserAddressTokoNow = UserAddressTokoNow()
)

data class UserAddressTokoNow(
        @SerializedName("shop_id")
        val shopId: String = "",
        @SerializedName("warehouse_id")
        val warehouseId: String = ""
)