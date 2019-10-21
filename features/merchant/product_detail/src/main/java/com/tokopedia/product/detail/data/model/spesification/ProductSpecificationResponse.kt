package com.tokopedia.product.detail.data.model.spesification


import com.google.gson.annotations.SerializedName

data class ProductSpecificationResponse(
    @SerializedName("ProductCatalogQuery")
    val productCatalogQuery: ProductCatalogQuery = ProductCatalogQuery()
)