package com.tokopedia.product.detail.data.model

import com.tokopedia.product.detail.data.model.ticker.GeneralTickerDataModel
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.AddressModel
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.RatesModel
import com.tokopedia.product.detail.estimasiongkir.data.model.v3.SummaryText

data class ProductInfoP3(
        var rateEstSummarizeText: SummaryText? = null,
        var ratesModel: RatesModel? = null,
        var addressModel: AddressModel? = null,
        var tickerInfo: List<GeneralTickerDataModel.TickerDetailDataModel> = ArrayList()
)