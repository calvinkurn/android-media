package com.tokopedia.product.detail.data.model.shopfeature

import com.google.gson.annotations.SerializedName

data class ShopFeature(
        @SerializedName("data")
        val `data`: ShopFeatureData = ShopFeatureData()
)