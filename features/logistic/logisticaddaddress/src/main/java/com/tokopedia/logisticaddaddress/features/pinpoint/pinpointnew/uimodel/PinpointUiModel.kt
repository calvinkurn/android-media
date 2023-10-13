package com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PinpointUiModel(
    var districtName: String = "",
    var cityName: String = "",
    var provinceName: String = "",
    var districtId: Long = 0L,
    var cityId: Long = 0L,
    var provinceId: Long = 0L,
    var postalCode: String = "",
    var lat: Double = 0.0,
    var long: Double = 0.0,
    var placeId: String = "",
    var title: String = "",
    var formattedAddress: String = ""
) : Parcelable {

    val selectedDistrict: String
        get() = formattedAddress

    fun hasPinpoint(): Boolean {
        return lat.isValidPinpoint() && long.isValidPinpoint()
    }

    val hasDistrictAndCityName: Boolean
        get() = districtName.isNotBlank() && cityName.isNotBlank()

    private fun Double.isValidPinpoint(): Boolean {
        return this != 0.0
    }
}
