package com.tokopedia.product.detail.data.model.shopfeature

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopFeature(
        @SerializedName("data")
        @Expose
        val `data`: ShopFeatureData = ShopFeatureData()
)