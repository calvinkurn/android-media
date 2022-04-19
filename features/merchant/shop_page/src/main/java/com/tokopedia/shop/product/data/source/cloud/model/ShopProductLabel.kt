package com.tokopedia.shop.product.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopProductLabel {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("color")
    @Expose
    var color: String? = null
}