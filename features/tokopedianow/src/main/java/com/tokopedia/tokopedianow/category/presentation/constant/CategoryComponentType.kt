package com.tokopedia.tokopedianow.category.presentation.constant

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    CategoryComponentType.HEADLINE_L1,
    CategoryComponentType.TABS_HORIZONTAL_SCROLL,
    CategoryComponentType.FEATURED_PRODUCT,
    CategoryComponentType.STATIC_TEXT,
    CategoryComponentType.PRODUCT_LIST_FILTER,
    CategoryComponentType.PRODUCT_LIST_INFINITE_SCROLL
)
annotation class CategoryComponentType {
    companion object {
        const val HEADLINE_L1 = "headline-l1"
        const val TABS_HORIZONTAL_SCROLL = "tabs-horizontal-scroll"
        const val FEATURED_PRODUCT = "featured-product"
        const val STATIC_TEXT = "static-text"
        const val PRODUCT_LIST_FILTER = "product-list-filter"
        const val PRODUCT_LIST_INFINITE_SCROLL = "product-list-infinite-scroll"
    }
}
