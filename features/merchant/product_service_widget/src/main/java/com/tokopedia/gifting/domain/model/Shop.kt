package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

class Shop {
    @SerializedName("Name")
    @Expose
    var name: String = ""

    @SerializedName("ShopTier")
    @Expose
    var shopTier: Long = Int.ZERO.toLong()

    @SerializedName("ShopType")
    @Expose
    var shopType: Long = Int.ZERO.toLong()
}