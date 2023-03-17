package com.tokopedia.logisticCommon.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetDistrictResponse(
    @SerializedName("kero_places_get_district")
    var keroPlacesGetDistrict: KeroPlacesGetDistrict = KeroPlacesGetDistrict()
)

data class KeroPlacesGetDistrict(
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
    @SuppressLint("Invalid Data Type")
    @SerializedName("city_id")
    var cityId: Long = 0,
    @SuppressLint("Invalid Data Type")
    @SerializedName("district_id")
    var districtId: Long = 0,
    @SerializedName("district_name")
    var districtName: String = "",
    @SerializedName("province_name")
    var provinceName: String = "",
    @SerializedName("city_name")
    var cityName: String = "",
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
    @SuppressLint("Invalid Data Type")
    @SerializedName("province_id")
    var provinceId: Long = 0,
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
