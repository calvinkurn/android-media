package com.tokopedia.shop.common.data.source.cloud.model.productlist

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Price?,
    @SerializedName("stock")
    val stock: Int?,
    @SerializedName("status")
    val status: ProductStatus?,
    @SerializedName("featured")
    val featured: Int = 0,
    @SerializedName("isVariant")
    val isVariant: Boolean?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("sku")
    val sku: String?,
    @SerializedName("pictures")
    val pictures: List<Picture>?
)