package com.tokopedia.product.detail.data.model

import com.tokopedia.product.detail.data.model.ticker.GeneralTickerDataModel

data class ProductInfoP3(
        var tickerInfo: List<GeneralTickerDataModel.TickerDetailDataModel> = ArrayList()
)