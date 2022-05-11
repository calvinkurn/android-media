package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
data class GlobalSearchShopUiModel(
    val shops: List<ShopUiModel>,
    val nextCursor: Int,
    val state: PagedState,
    val query: String,
) {

    companion object {
        val Empty = GlobalSearchShopUiModel(
            shops = emptyList(),
            nextCursor = 0,
            state = PagedState.Unknown,
            query = "pokemon", /** TODO: gonna change this later */
        )
    }
}