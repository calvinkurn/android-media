package com.tokopedia.localizationchooseaddress.util.request

import com.google.gson.annotations.SerializedName

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
) {
        companion object {
                const val MODE_ADDRESS = 1
                const val MODE_SNIPPET = 2
        }
}