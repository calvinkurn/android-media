package com.tokopedia.content.product.preview.view.uimodel.pager

data class ProductPreviewTabUiModel(
    val tabs: ProductReviewTab
) {
    enum class ProductReviewTab(
        val key: String
    ) {
        ProductTab(key = TAB_PRODUCT_KEY),
        ReviewTab(key = TAB_REVIEW_KEY)
    }

    companion object {
        const val TAB_PRODUCT_KEY = "product_tab"
        const val TAB_REVIEW_KEY = "review_tab"
        const val TAB_PRODUCT_POS = 0
        const val TAB_REVIEW_POS = 1

        val productTab = listOf(
            ProductPreviewTabUiModel(
                tabs = ProductReviewTab.ProductTab
            )
        )

        val reviewTab = listOf(
            ProductPreviewTabUiModel(
                tabs = ProductReviewTab.ReviewTab
            )
        )
    }
}
