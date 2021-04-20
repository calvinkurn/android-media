package com.tokopedia.logisticCommon.data.entity.response
import com.google.gson.annotations.SerializedName


data class AutoFillResponse(
    @SerializedName("kero_maps_autofill")
    var keroMapsAutofill: KeroMapsAutofill = KeroMapsAutofill()
)

data class KeroMapsAutofill(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("message_error")
    var messageError: List<String> = listOf(),
    @SerializedName("status")
    var status: String = "",
    @SerializedName("error_code")
    var errorCode: Int = 0
)

data class Data(
    @SerializedName("city_id")
    var cityId: Int = 0,
    @SerializedName("district_id")
    var districtId: Int = 0,
    @SerializedName("district_name")
    var districtName: String = "",
    @SerializedName("formatted_address")
    var formattedAddress: String = "",
    @SerializedName("full_data")
    var fullData: List<FullData> = listOf(),
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = "",
    @SerializedName("postal_code")
    var postalCode: String = "",
    @SerializedName("province_id")
    var provinceId: Int = 0,
    @SerializedName("title")
    var title: String = ""
)

data class FullData(
    @SerializedName("long_name")
    var longName: String = "",
    @SerializedName("short_name")
    var shortName: String = "",
    @SerializedName("types")
    var types: List<String> = listOf()
)