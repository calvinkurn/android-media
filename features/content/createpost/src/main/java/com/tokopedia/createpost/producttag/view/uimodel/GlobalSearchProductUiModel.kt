package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
data class GlobalSearchProductUiModel(
    val products: List<ProductUiModel>,
    val nextCursor: String,
    val state: PagedState,
    val query: String,
) {

    companion object {
        val Empty = GlobalSearchProductUiModel(
            products = emptyList(),
            nextCursor = "",
            state = PagedState.Unknown,
            query = "pokemon", /** TODO: gonna change this later */
        )
    }
}