package com.tokopedia.product.detail.common.data.model.rates

import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 23/02/21
 */
data class UserLocationRequest(
        @SerializedName("districtID")
        val districtID: String = "",
        @SerializedName("addressID")
        val addressID: String = "",
        @SerializedName("postalCode")
        val postalCode: String = "",
        @SerializedName("latlon")
        val latlon: String = ""
)