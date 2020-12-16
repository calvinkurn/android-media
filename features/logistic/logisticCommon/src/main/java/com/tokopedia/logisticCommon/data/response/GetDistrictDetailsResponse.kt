package com.tokopedia.logisticCommon.data.response

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

        @SerializedName("province_id")
        val provinceId: Int = 0,

        @SerializedName("district_id")
        val districtId: Int = 0,

        @SerializedName("zip_code")
        val zipCode: List<String> = emptyList(),

        @SerializedName("city_id")
        val cityId: Int = 0,

        @SerializedName("province_name")
        val provinceName: String = ""
)