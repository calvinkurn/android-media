package com.tokopedia.shop.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopQuestGeneralTrackerInput(
        @SerializedName("shop_id")
        @Expose
        var shopId: String = "",
        @SerializedName("product_id")
        @Expose
        var productId: String = "",
)