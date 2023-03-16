package com.tokopedia.product_ar.model

data class ProductArUiModel(
        val optionBgImage: String = "",
        val options: Map<String, ProductAr> = mapOf(),
        val provider: String = "",
        val metaData: ArMetaData = ArMetaData()
) {
    fun getProductArDataByProductId(produdctId: String): ProductAr {
        return options[produdctId] ?: ProductAr()
    }
}
