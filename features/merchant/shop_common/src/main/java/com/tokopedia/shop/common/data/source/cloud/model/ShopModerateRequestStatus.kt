package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopModerateRequestStatus(
        @SerializedName("result")
        @Expose
        val result: ShopModerateRequestResult = ShopModerateRequestResult(),

        @SerializedName("error")
        @Expose
        val error: ShopModerateRequestError = ShopModerateRequestError()
)