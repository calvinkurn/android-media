package com.tokopedia.shop.common.data.source.cloud.model.productlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("totalHits")
    @Expose
    val totalHits: Int? = 0
)