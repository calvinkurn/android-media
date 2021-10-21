package com.tokopedia.logisticCommon.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetDefaultAddressResponse (
        @SerializedName("KeroAddrGetDefaultAddress")
        var response: GetDefaultAddress = GetDefaultAddress()
)

data class GetDefaultAddress(
        @SerializedName("data")
        var data: DefaultAddressData = DefaultAddressData()
)

data class DefaultAddressData(
        @SuppressLint("Invalid Data Type")
        @SerializedName("addr_id")
        var addressId: Long = 0,
        @SerializedName("receiver_name")
        var receiverName: String = "",
        @SerializedName("addr_name")
        var addressName: String = "",
        @SerializedName("address_1")
        var address1: String = "",
        @SerializedName("address_2")
        var address2: String = "",
        @SerializedName("postal_code")
        var postalCode: String = "",
        @SerializedName("province")
        var provinceId: Int = 0,
        @SerializedName("city")
        var cityId: Int = 0,
        @SerializedName("district")
        var districtId: Int = 0,
        @SerializedName("phone")
        var phone: String = "",
        @SerializedName("province_name")
        var provinceName: String = "",
        @SerializedName("city_name")
        var cityName: String = "",
        @SerializedName("district_name")
        var districtName: String = "",
        @SerializedName("status")
        var status: Int = 0,
        @SerializedName("country")
        var country: String = "",
        @SerializedName("latitude")
        var latitude: String = "",
        @SerializedName("longitude")
        var longitude: String = ""
)