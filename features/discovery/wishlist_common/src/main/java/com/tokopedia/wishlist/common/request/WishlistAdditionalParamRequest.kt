package com.tokopedia.wishlist.common.request

import com.google.gson.annotations.SerializedName

data class WishlistAdditionalParamRequest(
        @SerializedName("district_id")
        val districtId: String = "",
        @SerializedName("city_id")
        val cityId: String = "",
        @SerializedName("latitude")
        val latitude: String = "",
        @SerializedName("longitude")
        val longitude: String = "",
        @SerializedName("postal_code")
        val postalCode: String = "",
        @SerializedName("address_id")
        val addressId: String = ""
)