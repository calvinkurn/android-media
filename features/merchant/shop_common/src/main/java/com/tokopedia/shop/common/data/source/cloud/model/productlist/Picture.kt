package com.tokopedia.shop.common.data.source.cloud.model.productlist

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("urlThumbnail")
    val urlThumbnail: String? = null
)