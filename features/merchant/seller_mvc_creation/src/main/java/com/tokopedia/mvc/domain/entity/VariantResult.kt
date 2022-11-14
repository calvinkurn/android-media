package com.tokopedia.mvc.domain.entity

data class VariantResult(
    val selections: List<Selection>,
    val products: List<Variant>
) {
    data class Selection(val options : List<Option>) {
        data class Option(val value : String)
    }
}
