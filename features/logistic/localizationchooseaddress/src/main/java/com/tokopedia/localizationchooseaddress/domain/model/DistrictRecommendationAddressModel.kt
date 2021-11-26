package com.tokopedia.localizationchooseaddress.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DistrictRecommendationAddressModel (
        var districtId: Long = 0,
        var districtName: String = "",
        var cityId: Long = 0,
        var cityName: String = "",
        var provinceId: Long = 0,
        var provinceName: String = "",
        var provinceCode: ArrayList<String> = arrayListOf()
) : Parcelable