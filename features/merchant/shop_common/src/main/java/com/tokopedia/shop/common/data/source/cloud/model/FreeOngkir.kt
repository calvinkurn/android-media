package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FreeOngkir(
        @SerializedName(value = "is_active", alternate = ["isActive"])
        @Expose
        val isActive: Boolean = false,

        @SerializedName(value = "img_url", alternate = ["imgURL"])
        @Expose
        val imgUrl: String = ""
)