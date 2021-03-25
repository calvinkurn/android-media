package com.tokopedia.localizationchooseaddress.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DistrictRecommendationAddressModel (
        var districtId: Int = 0,
        var districtName: String = "",
        var cityId: Int = 0,
        var cityName: String = "",
        var provinceId: Int = 0,
        var provinceName: String = "",
        var provinceCode: ArrayList<String> = arrayListOf()
) : Parcelable