package com.tokopedia.product.addedit.variant.presentation.model

import java.math.BigInteger

data class VariantInputModel(
        val products: List<ProductVariant> = listOf(),
        val selections: List<Selection> = listOf(),
        val sizecharts: List<Picture> = listOf()
)

data class ProductVariant(
        val combination: List<Int> = listOf(),
        val pictures: List<Picture> = listOf(),
        val price: BigInteger = 0.toBigInteger(),
        val sku: String = "",
        val status: String = "",
        val stock: Int = 0
)

data class Selection(
        val variantId: String = "",
        val unitID: String = "",
        val options: List<Option> = listOf()
)

data class Option(
        val unitValueID: String = "",
        val value: String = "",
        val hexCode: String = ""
)

data class Picture(
        val picID: String = "",
        val description: String = "",
        val filePath: String = "",
        val fileName: String = "",
        val width: Long = 0,
        val height: Long = 0,
        val isFromIG: String = "",
        val urlOriginal: String = "",
        val urlThumbnail: String = "",
        val url300: String = "",
        val status: Boolean = false,
        val uploadId: String = ""
)