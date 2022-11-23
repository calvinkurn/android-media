package com.tokopedia.tokopedianow.repurchase.constant

import androidx.annotation.StringDef
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.PRODUCT_RECOM_OOC
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_HISTORY_FILTER
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_HISTORY_SEARCH
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_NO_RESULT
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.EMPTY_STATE_OOC
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.ERROR_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.PRODUCT_RECOM_NO_RESULT
import com.tokopedia.tokopedianow.repurchase.constant.RepurchaseStaticLayoutId.Companion.SORT_FILTER

/**
 * IDs for layout that does not returned by Home Dynamic Channel query.
 * For example: Choose Address Widget. Home Dynamic Channel GQL does not
 * return Choose Address Widget, so the ID should be defined here.
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(
        CHOOSE_ADDRESS_WIDGET_ID,
        EMPTY_STATE_NO_RESULT,
        EMPTY_STATE_NO_HISTORY_SEARCH,
        EMPTY_STATE_NO_HISTORY_FILTER,
        EMPTY_STATE_OOC,
        ERROR_STATE_FAILED_TO_FETCH_DATA,
        PRODUCT_RECOM_OOC,
        PRODUCT_RECOM_NO_RESULT,
        SORT_FILTER
)
annotation class RepurchaseStaticLayoutId {
    companion object {
        const val CHOOSE_ADDRESS_WIDGET_ID = "0"
        const val EMPTY_STATE_NO_RESULT = "1"
        const val EMPTY_STATE_NO_HISTORY_SEARCH = "2"
        const val EMPTY_STATE_NO_HISTORY_FILTER = "3"
        const val EMPTY_STATE_OOC = "4"
        const val ERROR_STATE_FAILED_TO_FETCH_DATA = "5"
        const val PRODUCT_RECOM_OOC = "6"
        const val PRODUCT_RECOM_NO_RESULT = "7"
        const val SORT_FILTER = "8"
    }
}
