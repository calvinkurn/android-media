package com.tokopedia.product.detail.common.data.model.rates

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 23/02/21
 */
data class UserLocationRequest(
        @SerializedName("districtID")
        @Expose
        val districtID: String = "",
        @SerializedName("addressID")
        @Expose
        val addressID: String = "",
        @SerializedName("postalCode")
        @Expose
        val postalCode: String = "",
        @SerializedName("latlon")
        @Expose
        val latlon: String = "",
        @SerializedName("cityID")
        @Expose
        val cityId: String = ""
)