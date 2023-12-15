package com.tokopedia.logisticaddaddress.features.pinpoint.uimodel

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
    var title: String = ""
) : Parcelable {

    val selectedDistrict: String
        get() = formattedAddress

    val formattedAddress: String
        get() = listOf(districtName, cityName, provinceName).filter { it.isNotEmpty() }
            .joinToString(separator = ", ")

    fun hasPinpoint(): Boolean {
        return lat.isValidPinpoint() && long.isValidPinpoint()
    }

    val hasDistrictAndCityName: Boolean
        get() = districtName.isNotBlank() && cityName.isNotBlank()

    fun isNotEmpty(): Boolean {
        return districtName.isNotEmpty() && cityName.isNotEmpty() && provinceName.isNotEmpty() && lat != 0.0 && long != 0.0 && cityId != 0L && provinceId != 0L
    }

    private fun Double.isValidPinpoint(): Boolean {
        return this != 0.0
    }
}
