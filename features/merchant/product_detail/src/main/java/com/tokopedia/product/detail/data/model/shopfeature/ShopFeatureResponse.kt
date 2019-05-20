package com.tokopedia.product.detail.data.model.shopfeature

import com.google.gson.annotations.SerializedName

data class ShopFeatureResponse(
        @SerializedName("shopFeature")
        val shopFeature: ShopFeature = ShopFeature()
)