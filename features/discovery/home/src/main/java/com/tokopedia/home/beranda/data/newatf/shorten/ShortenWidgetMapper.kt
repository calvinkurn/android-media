package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.visitable.shorten.DealsAndMissionWidgetUiModel

object ShortenWidgetMapper {

    fun to2SquareUiModel(data: DynamicHomeChannel, atfData: AtfData): DealsAndMissionWidgetUiModel {
        val mission = data.channels.first()
        val deals = data.channels.last()

        return DealsAndMissionWidgetUiModel(
            id = atfData.atfMetadata.id.toString(),
            showShimmering = atfData.atfMetadata.isShimmer,
            deals = TwoSquareDealWidgetMapper.map(data, deals),
            mission = TwoSquareMissionWidgetMapper.map(data, mission)
        )
    }
}
