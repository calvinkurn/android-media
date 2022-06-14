package com.tokopedia.shopdiscount.info.data.uimodel

import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.unifycomponents.ticker.TickerData


data class ShopDiscountTickerUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val listTicker: List<TickerData> = listOf()
)