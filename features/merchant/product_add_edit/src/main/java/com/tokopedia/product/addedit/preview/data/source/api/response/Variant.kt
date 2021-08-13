package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class Variant(
        @SerializedName("products")
        val products: List<ProductVariant> = listOf(),
        @SerializedName("selections")
        val selections: List<Selection> = listOf(),
        @SerializedName("sizecharts")
        val sizecharts: List<Picture> = listOf()
)

data class ProductVariant(
        @SerializedName("status")
        val status: String,
        @SerializedName("productID")
        val id: String,
        @SerializedName("combination")
        val combination: List<Int>,
        @SerializedName("isPrimary")
        val isPrimary: Boolean,
        @SerializedName("price")
        val price: BigInteger,
        @SerializedName("sku")
        val sku: String,
        @SerializedName("stock")
        val stock: Int,
        @SerializedName("pictures")
        val pictures: List<Picture>
)

data class Selection(
        @SerializedName("variantID")
        val variantId: String,
        @SerializedName("variantName")
        val variantName: String,
        @SerializedName("unitName")
        val unitName: String,
        @SerializedName("unitID")
        val unitID: String,
        @SerializedName("identifier")
        val identifier: String,
        @SerializedName("options")
        val options: List<Option>
)

data class Option(
        @SerializedName("unitValueID")
        val unitValueID: String,
        @SerializedName("value")
        val value: String,
        @SerializedName("hexCode")
        val hexCode: String
)