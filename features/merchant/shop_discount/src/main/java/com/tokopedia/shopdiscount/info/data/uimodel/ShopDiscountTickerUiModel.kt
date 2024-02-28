package com.tokopedia.shopdiscount.info.data.uimodel

import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.unifycomponents.ticker.TickerData


data class ShopDiscountTickerUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val listTicker: List<TickerData> = listOf(),
    val tickerUnificationConfig: TickerUnificationConfig = TickerUnificationConfig()
){
    data class TickerUnificationConfig(
        val target: Target = Target()
    ){
        data class Target(
            val type: String = "",
            val listValue: List<String> = listOf()
        )
    }
}
