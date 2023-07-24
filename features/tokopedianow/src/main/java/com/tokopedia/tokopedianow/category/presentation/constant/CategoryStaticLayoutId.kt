package com.tokopedia.tokopedianow.category.presentation.constant

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    CategoryStaticLayoutId.LOAD_MORE_PROGRESS_BAR
)
annotation class CategoryStaticLayoutId {
    companion object {
        const val LOAD_MORE_PROGRESS_BAR = "load_more_progress_bar"
    }
}
