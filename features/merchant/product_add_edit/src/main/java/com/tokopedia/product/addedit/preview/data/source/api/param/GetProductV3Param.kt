package com.tokopedia.product.addedit.preview.data.source.api.param

data class GetProductV3Param(
        val productID: String,
        val options: OptionV3
)

data class OptionV3(
        val edit: Boolean = true,
        val category: Boolean = true,
        val catalog: Boolean = true,
        val wholesale: Boolean = true,
        val preorder: Boolean = true,
        val picture: Boolean = true,
        val sku: Boolean = true,
        val video: Boolean = true,
        val variant: Boolean = true
)