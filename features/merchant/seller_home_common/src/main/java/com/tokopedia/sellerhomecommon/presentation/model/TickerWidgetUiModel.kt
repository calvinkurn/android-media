package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 10/08/20
 */

data class TickerWidgetUiModel(
        override val widgetType: String = "",
        override val title: String = DATA_KEY,
        override val subtitle: String = "",
        override val tooltip: TooltipUiModel? = null,
        override val url: String = "",
        override val appLink: String = "",
        override val dataKey: String = DATA_KEY,
        override val ctaText: String = "",
        override var data: TickerDataUiModel? = null,
        override val impressHolder: ImpressHolder = ImpressHolder(),
        override var isLoaded: Boolean = false
) : BaseWidgetUiModel<TickerDataUiModel> {

    companion object {
        private const val DATA_KEY = "statistic_page_ticker"
    }

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}