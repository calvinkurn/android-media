package com.tokopedia.product.detail.data.model.shopfeature

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopFeatureResponse(
        @SerializedName("shopFeature")
        @Expose
        val shopFeature: ShopFeature = ShopFeature()
)