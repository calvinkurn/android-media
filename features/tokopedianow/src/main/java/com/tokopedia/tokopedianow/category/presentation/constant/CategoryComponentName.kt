package com.tokopedia.tokopedianow.category.presentation.constant

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    CategoryComponentName.TABS_HORIZONTAL_SCROLL,
    CategoryComponentName.PRODUCT_LIST_FILTER
)
annotation class CategoryComponentName {
    companion object {
        const val TABS_HORIZONTAL_SCROLL = "tabs-horizontal-scroll"
        const val PRODUCT_LIST_FILTER = "product-list-filter"
    }
}
