package com.tokopedia.product.manage.common.feature.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Variant (
        @Expose
    @SerializedName("products")
    val products: List<Product> = listOf(),
        @Expose
    @SerializedName("selections")
    val selections: List<Selection> = listOf(),
        @Expose
    @SerializedName("sizecharts")
    val sizeCharts: List<Picture> = listOf()
)