package com.tokopedia.dilayanitokopedia.home.domain.model.common

import com.google.gson.annotations.SerializedName

/**
 * Created by irpan on 30/11/22.
 */
data class FreeOngkir(
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("isActive")
    val isActive: Boolean = false
)
