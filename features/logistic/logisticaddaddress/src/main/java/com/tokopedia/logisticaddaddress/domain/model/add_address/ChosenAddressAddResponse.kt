package com.tokopedia.logisticaddaddress.domain.model.add_address

import com.google.gson.annotations.SerializedName

data class ChosenAddressAddResponse(
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