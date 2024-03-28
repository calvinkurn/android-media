package com.tokopedia.home.beranda.data.newatf.ticker.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.model.GetTargetedTicker
import com.tokopedia.home.beranda.data.model.GetTargetedTickerItem
import com.tokopedia.home.beranda.data.model.TargetedTicker
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.domain.model.TargetedTickerUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment
import com.tokopedia.unifycomponents.ticker.TickerData

object TargetedTickerMapper {

    fun map(data: GetTargetedTicker): List<TargetedTickerUiModel> {
        return data.tickers.map {
            TargetedTickerUiModel(
                id = it.id,
                title = it.title,
                content = it.content,
                type = mapTickerType(it),
                priority = it.priority,
                action = TargetedTickerUiModel.Action(
                    label = it.action.label,
                    type = it.action.type,
                    appLink = it.action.appLink,
                    url = it.action.url,
                ),
            )
        }
    }

    fun asVisitable(
        data: GetTargetedTicker,
        atfData: AtfData
    ): Visitable<*>? {
        if (atfData.isCache) return null
        if (HomeRevampFragment.HIDE_TICKER) return null

        val uiModel = map(data)

        return TickerDataModel(
            targetedTickers = map(data),
            unifyTickers = uiModel.map {
                TickerData(
                    title = it.title,
                    description = it.content,
                    type = it.type
                )
            }
        )
    }

    private fun mapTickerType(ticker: GetTargetedTickerItem): Int = when (ticker.getTickerType()) {
        is GetTargetedTickerItem.Type.Info -> com.tokopedia.unifycomponents.ticker.Ticker.TYPE_INFORMATION
        is GetTargetedTickerItem.Type.Warning -> com.tokopedia.unifycomponents.ticker.Ticker.TYPE_WARNING
        is GetTargetedTickerItem.Type.Danger -> com.tokopedia.unifycomponents.ticker.Ticker.TYPE_ERROR
    }
}
