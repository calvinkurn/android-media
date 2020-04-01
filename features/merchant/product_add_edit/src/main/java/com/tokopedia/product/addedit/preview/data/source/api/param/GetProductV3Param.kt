package com.tokopedia.product.addedit.preview.data.source.api.param

data class GetProductV3Param(
        val productID: String,
        val options: OptionV3
)

data class OptionV3(
        val edit: Boolean,
        val variant: Boolean
)