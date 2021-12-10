package com.tokopedia.product_ar.model

data class ProductArUiModel(
        val optionBgImage: String = "",
        val options: Map<String, ProductAr> = mapOf(),
        val provider: String = ""
)
