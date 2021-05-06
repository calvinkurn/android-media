package com.tokopedia.localizationchooseaddress.domain.response

import com.google.gson.annotations.SerializedName

data class GetDefaultChosenAddressGqlResponse (
        @SerializedName("keroAddrGetDefaultChosenAddress")
        var response: GetDefaultChosenAddressResponse = GetDefaultChosenAddressResponse()
)

data class GetDefaultChosenAddressResponse(
        @SerializedName("data")
        var data: DefaultChosenAddressData = DefaultChosenAddressData(),
        @SerializedName("kero_addr_error")
        var error: ErrorDefaultAddress = ErrorDefaultAddress(),
        @SerializedName("status")
        var status: String = "",
        @SerializedName("server_process_time")
        var serverProcessTime: String = "",
        @SerializedName("config")
        var config: String = ""
)

data class DefaultChosenAddressData(
        @SerializedName("addr_id")
        var addressId: Int = 0,
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
        @SerializedName("coountry")
        var country: String = "",
        @SerializedName("latitude")
        var latitude: String = "",
        @SerializedName("longitude")
        var longitude: String = ""
)

data class ErrorDefaultAddress(
        @SerializedName("code")
        var code: Int = 0,
        @SerializedName("detail")
        var detail: String = ""
)