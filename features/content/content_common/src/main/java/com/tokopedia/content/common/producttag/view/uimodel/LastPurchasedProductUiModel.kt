package com.tokopedia.content.common.producttag.view.uimodel

import com.tokopedia.content.common.producttag.view.uimodel.ProductUiModel

/**
 * Created By : Jonathan Darwin on April 28, 2022
 */
data class LastPurchasedProductUiModel(
    val products: List<ProductUiModel>,
    val nextCursor: String,
    val state: PagedState,
    val coachmark: String,
    val isCoachmarkShown: Boolean,
) {

    companion object {
        val Empty: LastPurchasedProductUiModel
            get() = LastPurchasedProductUiModel(
                products = emptyList(),
                nextCursor = "",
                state = PagedState.Unknown,
                coachmark = "",
                isCoachmarkShown = false,
            )
    }
}