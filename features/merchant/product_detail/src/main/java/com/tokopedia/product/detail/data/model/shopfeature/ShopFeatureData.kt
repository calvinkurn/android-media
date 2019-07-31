package com.tokopedia.product.detail.data.model.shopfeature

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopFeatureData(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("value")
        @Expose
        val value: Boolean = false
)