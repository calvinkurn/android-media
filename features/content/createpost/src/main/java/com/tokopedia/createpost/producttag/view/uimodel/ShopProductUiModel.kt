package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
data class ShopProductUiModel(
    val shop: ShopUiModel,
    val products: List<ProductUiModel>,
    val nextCursor: Int,
    val state: PagedState,
    val query: String,
) {

    companion object {
        val Empty = ShopProductUiModel(
            shop = ShopUiModel(),
            products = emptyList(),
            nextCursor = 0,
            state = PagedState.Unknown,
            query = "",
        )
    }
}