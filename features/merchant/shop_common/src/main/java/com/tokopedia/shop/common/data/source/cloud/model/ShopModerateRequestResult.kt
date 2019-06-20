package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopModerateRequestResult(
        @SerializedName("shopID")
        @Expose
        val shopID: String = "",

        @SerializedName("status")
        @Expose
        val status: Int = 0

)