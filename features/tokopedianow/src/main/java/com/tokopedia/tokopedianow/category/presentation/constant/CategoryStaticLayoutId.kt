package com.tokopedia.tokopedianow.category.presentation.constant

import androidx.annotation.StringDef

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    CategoryStaticLayoutId.TICKER_WIDGET_ID,
    CategoryStaticLayoutId.CHOOSE_ADDRESS,
    CategoryStaticLayoutId.CATEGORY_MENU_EMPTY_STATE,
    CategoryStaticLayoutId.LOAD_MORE_PROGRESS_BAR
)
annotation class CategoryStaticLayoutId {
    companion object {
        const val TICKER_WIDGET_ID = "ticker_widget"
        const val CHOOSE_ADDRESS = "choose_address"
        const val CATEGORY_MENU_EMPTY_STATE = "category_menu_empty_state"
        const val LOAD_MORE_PROGRESS_BAR = "load_more_progress_bar"
    }
}
