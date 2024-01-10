package com.tokopedia.content.product.preview.view.uimodel.pager

import com.tokopedia.content.product.preview.utils.TAB_PRODUCT_KEY
import com.tokopedia.content.product.preview.utils.TAB_REVIEW_KEY

data class ProductPreviewTabUiModel(val key: String) {
    companion object {
        val emptyProduct = listOf(
            ProductPreviewTabUiModel(
                key = TAB_REVIEW_KEY
            )
        )
        val withProduct = listOf(
            ProductPreviewTabUiModel(
                key = TAB_PRODUCT_KEY
            ),
            ProductPreviewTabUiModel(
                key = TAB_REVIEW_KEY
            )
        )
    }
}
