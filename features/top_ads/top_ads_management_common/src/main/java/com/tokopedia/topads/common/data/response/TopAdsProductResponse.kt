package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

data class TopAdsProductResponse(
    @SerializedName("getProductV3")
    val product: Product,
    @SerializedName("errors")
    val errors: List<TopAdsProductError>? = listOf()
)

data class Product(
    @SerializedName("productID")
    val productId: String,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("category")
    val category: Category?
)

data class TopAdsProductError(
    @SerializedName("message")
    val message: String
)

data class OptionV3(
    @SerializedName("edit")
    val edit: Boolean
)

data class Category(
    @SerializedName("id")
    val id : String,
    @SerializedName("name")
    val name : String,
    @SerializedName("title")
    val title : String,
    )
