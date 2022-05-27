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
        const val EMPTY_STATE_NO_PIN_POINT = "3"
        const val EMPTY_STATE_NO_ADDRESS = "4"
        const val LOADING_STATE = "5"
        const val PROGRESS_BAR = "6"
        const val MERCHANT_TITLE = "7"
    }
}