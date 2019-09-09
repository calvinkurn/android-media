package com.tokopedia.product.detail.data.model.spesification


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("catalog")
    val catalog: Catalog = Catalog()
)