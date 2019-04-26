package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopModerateError(

        @SerializedName("message")
        @Expose
        val message: String = ""

)
