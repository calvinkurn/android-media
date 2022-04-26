package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
data class LastTaggedProductUiModel(
    val products: List<ProductUiModel>,
    val nextCursor: String,
) {
    companion object {
        val Empty = LastTaggedProductUiModel(
            products = emptyList(),
            nextCursor = ""
        )
    }
}