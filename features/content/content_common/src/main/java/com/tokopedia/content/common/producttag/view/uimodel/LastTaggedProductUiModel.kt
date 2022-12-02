package com.tokopedia.content.common.producttag.view.uimodel

import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on April 26, 2022
 */
data class LastTaggedProductUiModel(
    val products: List<ProductUiModel>,
    val nextCursor: String,
    val state: PagedState,
) {

    companion object {
        val Empty: LastTaggedProductUiModel
            get() = LastTaggedProductUiModel(
                products = emptyList(),
                nextCursor = "",
                state = PagedState.Unknown,
            )
    }
}