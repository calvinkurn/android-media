package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductBadge(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("imageUrl")
    @Expose
    val imageUrl: String = "",

    @SerializedName("show")
    @Expose
    val show: Boolean = false
)
