package com.tokopedia.logisticaddaddress.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Irfan Khoirul on 17/11/17.
 */
@Parcelize
data class Address(
    var districtId: Long = 0,
    var districtName: String = "",
    var cityId: Long = 0,
    var cityName: String = "",
    var provinceId: Long = 0,
    var provinceName: String = "",
    var zipCodes: List<String> = arrayListOf(),
    var lat: Double = 0.0,
    var long: Double = 0.0
) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }
}
