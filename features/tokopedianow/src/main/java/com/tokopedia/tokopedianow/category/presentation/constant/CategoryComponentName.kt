package com.tokopedia.tokopedianow.category.presentation.constant

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    CategoryComponentName.TABS_HORIZONTAL_SCROLL
)
annotation class CategoryComponentName {
    companion object {
        const val TABS_HORIZONTAL_SCROLL = "tabs-horizontal-scroll"
    }
}
