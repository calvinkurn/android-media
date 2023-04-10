package com.tokopedia.dilayanitokopedia.common.constant

import androidx.annotation.StringDef
import com.tokopedia.dilayanitokopedia.common.constant.HomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.dilayanitokopedia.common.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_FAILED_TO_FETCH_DATA
import com.tokopedia.dilayanitokopedia.common.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE
import com.tokopedia.dilayanitokopedia.common.constant.HomeStaticLayoutId.Companion.EMPTY_STATE_OUT_OF_COVERAGE
import com.tokopedia.dilayanitokopedia.common.constant.HomeStaticLayoutId.Companion.LOADING_STATE
import com.tokopedia.dilayanitokopedia.common.constant.HomeStaticLayoutId.Companion.PRODUCT_RECOM_OOC
import com.tokopedia.dilayanitokopedia.common.constant.HomeStaticLayoutId.Companion.PROGRESS_BAR
import com.tokopedia.dilayanitokopedia.common.constant.HomeStaticLayoutId.Companion.SWITCH_SERVICE_WIDGET
import com.tokopedia.dilayanitokopedia.common.constant.HomeStaticLayoutId.Companion.TICKER_WIDGET_ID

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    CHOOSE_ADDRESS_WIDGET_ID,
    TICKER_WIDGET_ID,
    EMPTY_STATE_OUT_OF_COVERAGE,
    EMPTY_STATE_NO_ADDRESS_AND_LOCAL_CACHE,
    EMPTY_STATE_FAILED_TO_FETCH_DATA,
    LOADING_STATE,
    PRODUCT_RECOM_OOC,
    PROGRESS_BAR,
    SWITCH_SERVICE_WIDGET
)
annotation class HomeStaticLayoutId {
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
