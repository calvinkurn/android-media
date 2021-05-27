package com.tokopedia.tokomart.home.constant

import androidx.annotation.StringDef
import com.tokopedia.tokomart.home.constant.HomeAdditionalWidgetId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokomart.home.constant.HomeAdditionalWidgetId.Companion.TICKER_WIDGET_ID

@Retention(AnnotationRetention.SOURCE)
@StringDef(
        CHOOSE_ADDRESS_WIDGET_ID,
        TICKER_WIDGET_ID
)
annotation class HomeAdditionalWidgetId {
    companion object {
        const val CHOOSE_ADDRESS_WIDGET_ID = "0"
        const val TICKER_WIDGET_ID = "1"
    }
}