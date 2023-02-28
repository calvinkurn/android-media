package com.tokopedia.tokofood.common.address

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokoFoodChosenAddress(
    @SerializedName("mode")
    val mode: Int = 0,
    @SerializedName("address_id")
    val addressId: Long = 0,
    @SerializedName("district_id")
    val districtId: Long = 0,
    @SerializedName("postal_code")
    val postalCode: String = "",
    @SerializedName("geolocation")
    val geolocation: String = ""
) : Parcelable {
    companion object {
        const val MODE_EMPTY = 0
        const val MODE_ADDRESS = 1
        const val MODE_SNIPPET = 2
    }
}
