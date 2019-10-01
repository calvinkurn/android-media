package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FreeOngkir(
        @SerializedName("is_active")
        @Expose
        val isActive: Boolean = false,

        @SerializedName("img_url")
        @Expose
        val imgUrl: String = ""
)