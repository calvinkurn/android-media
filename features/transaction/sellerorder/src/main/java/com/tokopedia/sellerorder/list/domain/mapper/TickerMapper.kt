package com.tokopedia.sellerorder.list.domain.mapper

import com.tokopedia.sellerorder.list.domain.model.SomListGetTickerResponse
import com.tokopedia.sellerorder.list.presentation.models.SomListTickerUiModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import javax.inject.Inject

class TickerMapper @Inject constructor() {
    fun mapResponseToUiModel(orderTickers: SomListGetTickerResponse.Data.OrderTickers): List<TickerData> {
        return orderTickers.tickers.map {
            TickerData(
                    title = "",
                    description = it.body,
                    type = Ticker.TYPE_INFORMATION,
                    isFromHtml = true,
                    itemData = SomListTickerUiModel(
                            id = it.id,
                            body = it.body,
                            shortDesc = it.shortDesc,
                            isActive = it.isActive
                    )
            )
        }
    }
}