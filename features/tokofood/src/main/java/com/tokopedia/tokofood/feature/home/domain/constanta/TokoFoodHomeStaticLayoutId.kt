package com.tokopedia.tokofood.feature.home.domain.constanta

import androidx.annotation.StringDef
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_CATEGORY_PAGE
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_NO_PIN_POINT
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_OUT_OF_COVERAGE
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.ERROR_STATE
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.LOADING_STATE
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.MERCHANT_TITLE
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.PROGRESS_BAR
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.TICKER_WIDGET_ID

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    CHOOSE_ADDRESS_WIDGET_ID,
    TICKER_WIDGET_ID,
    EMPTY_STATE_OUT_OF_COVERAGE,
    EMPTY_STATE_NO_PIN_POINT,
    EMPTY_STATE_NO_ADDRESS,
    LOADING_STATE,
    EMPTY_STATE_CATEGORY_PAGE,
    PROGRESS_BAR,
    MERCHANT_TITLE,
    ERROR_STATE
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
        const val ERROR_STATE = "8"
        const val EMPTY_STATE_CATEGORY_PAGE = "9"
    }
}