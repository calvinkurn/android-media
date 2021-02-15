package com.tokopedia.cart.data.model.request

import com.google.gson.annotations.SerializedName

data class AdditionalParams(
        @SerializedName("chosen_address")
        val chosenAddress: ChosenAddress = ChosenAddress()
)

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
)