package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 10/08/20
 */

data class TickerDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = true,
    override val lastUpdated: Long = 0,
    var tickers: List<TickerItemUiModel> = emptyList()
) : BaseDataUiModel {

    override fun isWidgetEmpty(): Boolean {
        return tickers.isEmpty()
    }
}