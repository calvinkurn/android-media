package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.visitable.shorten.DealsAndMissionWidgetUiModel

object ShortenWidgetMapper {

    fun to2SquareUiModel(data: DynamicHomeChannel, atfData: AtfData): DealsAndMissionWidgetUiModel {
        val top2ChannelList = data.channels
            .takeIf { it.size <= 2 } ?: return DealsAndMissionWidgetUiModel()

        // TODO: have been over-comm to BE team to provide the variable for this determination.
        val mission = top2ChannelList.first()
        val deals = top2ChannelList.last()

        return DealsAndMissionWidgetUiModel(
            id = atfData.atfMetadata.id.toString(),
            showShimmering = atfData.atfMetadata.isShimmer,
            deals = TwoSquareDealWidgetMapper.map(data, deals),
            mission = TwoSquareMissionWidgetMapper.map(data, mission)
        )
    }
}
