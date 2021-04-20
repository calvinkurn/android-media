package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 10/08/20
 */

data class TickerDataUiModel(
        override val dataKey: String = "",
        override var error: String = "",
        var tickers: List<TickerItemUiModel> = emptyList(),
        override var isFromCache: Boolean = false,
        override val showWidget: Boolean = true
) : BaseDataUiModel {
    override fun shouldRemove(): Boolean {
        return tickers.isEmpty()
    }
}