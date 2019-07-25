package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopModerateShop(
        @SerializedName("success")
        @Expose
        val success: Boolean = false,

        @SerializedName("message")
        @Expose
        val message: String = ""
)