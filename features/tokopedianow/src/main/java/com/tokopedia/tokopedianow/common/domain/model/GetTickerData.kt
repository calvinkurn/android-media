package com.tokopedia.tokopedianow.common.domain.model

import com.tokopedia.unifycomponents.ticker.TickerData

data class GetTickerData(
    val blockAddToCart: Boolean = false,
    val tickerList: List<TickerData> = emptyList(),
    val oosTickerList: List<TickerData> = emptyList(),
    val oosCategoryIds: List<String> = emptyList()
)
