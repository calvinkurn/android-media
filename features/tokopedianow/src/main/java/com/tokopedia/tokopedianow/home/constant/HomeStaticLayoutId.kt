package com.tokopedia.tokopedianow.home.constant

import androidx.annotation.StringDef
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokopedianow.home.constant.HomeStaticLayoutId.Companion.TICKER_WIDGET_ID

/**
 * IDs for layout that does not returned by Home Dynamic Channel query.
 * For example: Choose Address Widget. Home Dynamic Channel GQL does not
 * return Choose Address Widget, so the ID should be defined here.
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(
        CHOOSE_ADDRESS_WIDGET_ID,
        TICKER_WIDGET_ID
)
annotation class HomeStaticLayoutId {
    companion object {
        const val CHOOSE_ADDRESS_WIDGET_ID = "0"
        const val TICKER_WIDGET_ID = "1"
        const val EMPTY_STATE_NO_ADDRESS = "2"
        const val EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE = "3"
        const val EMPTY_STATE_FAILED_TO_FETCH_DATA = "4"
        const val LOADING_STATE = "5"
        const val PRODUCT_RECOM_OOC = "6"
    }
}