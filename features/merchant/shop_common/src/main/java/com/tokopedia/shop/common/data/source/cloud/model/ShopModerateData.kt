package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopModerateData(
        @SerializedName("moderateShop")
        @Expose
        val moderateShop: ShopModerateShop = ShopModerateShop()
)