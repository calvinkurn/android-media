package com.tokopedia.home.beranda.data.newatf.ticker.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.data.model.TargetedTicker
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.domain.model.TargetedTickerUiModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.beranda.presentation.view.fragment.HomeRevampFragment

object TargetedTickerMapper {

    fun map(data: TargetedTicker): List<TargetedTickerUiModel> {
        return data.targetedTicker.tickers.map {
            TargetedTickerUiModel(
                id = it.id,
                title = it.title,
                content = it.content,
                type = it.type,
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
        data: TargetedTickerUiModel,
        atfData: AtfData
    ): Visitable<*>? {
        if (atfData.isCache) return null
        if (HomeRevampFragment.HIDE_TICKER) return null

        return TickerDataModel(
            targetedTickers = listOf(data)
        )
    }
}
