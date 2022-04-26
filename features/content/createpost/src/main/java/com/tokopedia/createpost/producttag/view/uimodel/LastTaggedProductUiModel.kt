package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
data class LastTaggedProductUiModel(
    val products: List<ProductUiModel>,
    val nextCursor: String = "",
    val state: PagedState,
) {

    companion object {
        val Empty = LastTaggedProductUiModel(
            products = emptyList(),
            state = PagedState.Unknown,
        )
    }
}