package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created By @ilhamsuaib on 10/08/20
 */

data class TickerDataUiModel(
        override val dataKey: String = "",
        override var error: String = "",
        var tickers: List<TickerItemUiModel> = emptyList()
) : BaseDataUiModel