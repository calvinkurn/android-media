package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.gm.common.data.source.local.model.TickerUiModel
import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 30/03/21
 */

class WidgetTickerUiModel(
        val tickers: List<TickerUiModel> = emptyList()
) : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}