package com.tokopedia.product.manage.feature.quickedit.variant.data.model

import com.google.gson.annotations.SerializedName

data class Variant (
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("selections")
    val selections: List<Selection>,
    @SerializedName("sizecharts")
    val sizeCharts: List<Picture>
)