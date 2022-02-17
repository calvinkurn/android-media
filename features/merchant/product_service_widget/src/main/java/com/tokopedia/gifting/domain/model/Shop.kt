package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Shop {
    @SerializedName("Name")
    @Expose
    var name: String? = null

    @SerializedName("ShopTier")
    @Expose
    var shopTier: Long? = null

    @SerializedName("ShopType")
    @Expose
    var shopType: Long? = null
}