package com.tokopedia.dilayanitokopedia.home.domain.model.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by irpan on 30/11/22.
 */
data class FreeOngkir(
    @SerializedName("imageUrl")
    @Expose
    val imageUrl: String = "",
    @SerializedName("isActive")
    @Expose
    val isActive: Boolean = false
)
