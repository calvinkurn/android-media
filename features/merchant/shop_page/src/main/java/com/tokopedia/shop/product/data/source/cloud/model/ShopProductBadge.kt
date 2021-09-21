package com.tokopedia.shop.product.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopProductBadge {
    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null
}