package com.tokopedia.logisticCommon.domain.model

import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import kotlinx.parcelize.Parcelize

sealed class AutoCompleteVisitable

data class Place(
    var data: List<SuggestedPlace> = listOf(),
    var errorCode: Int = 0
) : AutoCompleteVisitable()

@Parcelize
data class SuggestedPlace(
    val mainText: String = "",
    val secondaryText: String = "",
    val placeId: String = "",
    val districtName: String = "",
    val cityName: String = "",
    val provinceName: String = "",
    val districtId: Long = 0L,
    val cityId: Long = 0L,
    val provinceId: Long = 0L,
    val postalCode: String = "",
    val lat: String = "0.0",
    val long: String = "0.0",
    val title: String = ""
) : AutoCompleteVisitable(), Parcelable {
    val formattedAddress: String
        get() {
            return listOf(districtName, cityName, provinceName).filter { it.isNotEmpty() }
                .joinToString(separator = ", ")
        }

    fun hasPinpoint(): Boolean {
        return lat.isValidPinpoint() && long.isValidPinpoint()
    }
    private fun String.isValidPinpoint(): Boolean {
        return this.toDoubleOrZero() != 0.0
    }
}

data class SavedAddress(
    var addrId: Long = 0,
    var addrName: String = "",
    var address1: String = "",
    var latitude: String = "",
    var longitude: String = ""
) : AutoCompleteVisitable()

data class LoadingType(val id: Int = 0) : AutoCompleteVisitable()
data class NoResultType(val id: Int = 0) : AutoCompleteVisitable()
data class HeaderType(val id: Int = 0) : AutoCompleteVisitable()
