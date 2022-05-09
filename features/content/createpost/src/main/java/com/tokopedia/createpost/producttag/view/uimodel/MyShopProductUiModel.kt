package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on May 09, 2022
 */
data class MyShopProductUiModel(
    val products: List<ProductUiModel>,
    val nextCursor: String,
    val state: PagedState,
    val query: String,
) {

    companion object {
        val Empty = MyShopProductUiModel(
            products = emptyList(),
            nextCursor = "",
            state = PagedState.Unknown,
            query = "",
        )
    }
}