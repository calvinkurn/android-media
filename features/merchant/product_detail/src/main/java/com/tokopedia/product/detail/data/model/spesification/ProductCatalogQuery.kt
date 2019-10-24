package com.tokopedia.product.detail.data.model.spesification


import com.google.gson.annotations.SerializedName

data class ProductCatalogQuery(
        @SerializedName("data")
        val data: Data = Data(),
        @SerializedName("header")
        val header: Header = Header()
)