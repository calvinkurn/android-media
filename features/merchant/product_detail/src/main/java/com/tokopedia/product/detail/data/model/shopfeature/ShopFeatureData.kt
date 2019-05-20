package com.tokopedia.product.detail.data.model.shopfeature

import com.google.gson.annotations.SerializedName

data class ShopFeatureData(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("value")
        val value: Boolean = false
)