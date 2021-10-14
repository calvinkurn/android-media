package com.tokopedia.logisticCommon.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class GetDistrictDetailsResponse(
        @SerializedName("keroGetDistrictDetails")
        val keroDistrictDetails: KeroDistrictRecommendation = KeroDistrictRecommendation()
)

data class KeroDistrictRecommendation(

        @SerializedName("district")
        val district: List<DistrictItem> = emptyList(),

        @SerializedName("next_available")
        val nextAvailable: Boolean = false
)

data class DistrictItem(

        @SerializedName("district_name")
        val districtName: String = "",

        @SerializedName("city_name")
        val cityName: String = "",

        @SuppressLint("Invalid Data Type")
        @SerializedName("province_id")
        val provinceId: Long = 0,

        @SuppressLint("Invalid Data Type")
        @SerializedName("district_id")
        val districtId: Long = 0,

        @SerializedName("zip_code")
        val zipCode: List<String> = emptyList(),

        @SuppressLint("Invalid Data Type")
        @SerializedName("city_id")
        val cityId: Long = 0,

        @SerializedName("province_name")
        val provinceName: String = ""
)