package com.tokopedia.product.addedit.preview.data.source.api.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class Variant(
        @SerializedName("products")
        @Expose
        val products: List<ProductVariant> = listOf(),
        @SerializedName("selections")
        @Expose
        val selections: List<Selection> = listOf(),
        @SerializedName("sizecharts")
        @Expose
        val sizecharts: List<Picture> = listOf()
)

@SuppressLint("Invalid Data Type")
data class ProductVariant(
        @SerializedName("status")
        @Expose
        val status: String,
        @SerializedName("productID")
        @Expose
        val id: String,
        @SerializedName("combination")
        @Expose
        val combination: List<Int>,
        @SerializedName("isPrimary")
        @Expose
        val isPrimary: Boolean,
        @SerializedName("price")
        @Expose
        val price: BigInteger,
        @SerializedName("sku")
        @Expose
        val sku: String,
        @SerializedName("stock")
        @Expose
        val stock: Int,
        @SerializedName("pictures")
        @Expose
        val pictures: List<Picture>,
        @SerializedName("weight")
        @Expose
        val weight: Int,
        @SerializedName("weightUnit")
        @Expose
        val weightUnit: String
)

data class Selection(
        @SerializedName("variantID")
        @Expose
        val variantId: String,
        @SerializedName("variantName")
        @Expose
        val variantName: String,
        @SerializedName("unitName")
        @Expose
        val unitName: String,
        @SerializedName("unitID")
        @Expose
        val unitID: String,
        @SerializedName("identifier")
        @Expose
        val identifier: String,
        @SerializedName("options")
        @Expose
        val options: List<Option>
)

data class Option(
        @SerializedName("unitValueID")
        @Expose
        val unitValueID: String,
        @SerializedName("value")
        @Expose
        val value: String,
        @SerializedName("hexCode")
        @Expose
        val hexCode: String
)