package com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChosenAddress(
        @SerializedName("mode")
        val mode: Int = 0,
        @SerializedName("address_id")
        val addressId: String = "",
        @SerializedName("district_id")
        val districtId: String = "",
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