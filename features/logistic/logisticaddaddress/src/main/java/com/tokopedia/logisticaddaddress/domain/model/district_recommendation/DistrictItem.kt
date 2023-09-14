package com.tokopedia.logisticaddaddress.domain.model.district_recommendation

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

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
