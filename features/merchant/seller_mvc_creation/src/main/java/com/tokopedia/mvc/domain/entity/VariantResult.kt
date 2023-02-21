package com.tokopedia.mvc.domain.entity

data class VariantResult(
    val parentProductName: String,
    val parentProductPrice: Long,
    val parentProductStock: Int,
    val parentProductSoldCount: Int,
    val parentProductImageUrl: String,
    val selections: List<Selection>,
    val products: List<Variant>
) {
    data class Selection(val options: List<Option>) {
        data class Option(val value: String)
    }
}
