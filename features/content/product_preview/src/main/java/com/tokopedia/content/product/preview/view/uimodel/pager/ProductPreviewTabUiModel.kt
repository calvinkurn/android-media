package com.tokopedia.content.product.preview.view.uimodel.pager

data class ProductPreviewTabUiModel(val tabs: List<ProductReviewTab>) {

    data class ProductReviewTab(val key: String)

    companion object {
        const val TAB_PRODUCT_KEY = "product"
        const val TAB_REVIEW_KEY = "review"
        const val TAB_PRODUCT_POS = 0
        const val TAB_REVIEW_POS = 1

        val Empty = ProductPreviewTabUiModel(tabs = emptyList())
        val productTab = ProductReviewTab(key = TAB_PRODUCT_KEY)
        val reviewTab = ProductReviewTab(key = TAB_REVIEW_KEY)
    }
}
