package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 10/08/20
 */

data class TickerDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = true,
    var tickers: List<TickerItemUiModel> = emptyList()
) : BaseDataUiModel {

    override fun shouldRemove(): Boolean {
        return tickers.isEmpty()
    }
}