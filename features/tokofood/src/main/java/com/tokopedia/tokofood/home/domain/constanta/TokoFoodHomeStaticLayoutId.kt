package com.tokopedia.tokofood.home.domain.constanta

import androidx.annotation.StringDef
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.TICKER_WIDGET_ID

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    CHOOSE_ADDRESS_WIDGET_ID,
    TICKER_WIDGET_ID
)
annotation class TokoFoodHomeStaticLayoutId {
    companion object {
        const val CHOOSE_ADDRESS_WIDGET_ID = "0"
        const val TICKER_WIDGET_ID = "1"
        const val EMPTY_STATE_OUT_OF_COVERAGE = "2"
        const val EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE = "3"
        const val EMPTY_STATE_FAILED_TO_FETCH_DATA = "4"
        const val LOADING_STATE = "5"
        const val PRODUCT_RECOM_OOC = "6"
        const val PROGRESS_BAR = "7"
        const val SWITCH_SERVICE_WIDGET = "8"
    }
}