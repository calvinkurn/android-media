package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.visitable.shorten.DealsAndMissionWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.DealsWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemDealsWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel

object ShortenWidgetMapper {

    /**
     * 2 Square Mission and Deals widget
     */
    fun to2SquareUiModel(data: DynamicHomeChannel, atfData: AtfData): DealsAndMissionWidgetUiModel {
        val mission = data.channels.first()
        val deals = data.channels.last()

        return DealsAndMissionWidgetUiModel(
            id = data.channels.first().id,
            showShimmering = atfData.atfMetadata.isShimmer,
            deals = DealsWidgetUiModel(
                channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                    deals,
                    0
                ),
                header = ChannelHeader(
                    name = deals.header.name
                ),
                data = listOf(
                    ItemDealsWidgetUiModel("deals 1"),
                    ItemDealsWidgetUiModel("deals 2"),
                )
            ),
            mission = MissionWidgetUiModel(
                channelModel = DynamicChannelComponentMapper.mapHomeChannelToComponent(
                    mission,
                    1
                ),
                header = ChannelHeader(
                    name = mission.header.name
                ),
                data = listOf(
                    ItemMissionWidgetUiModel("mission 1"),
                    ItemMissionWidgetUiModel("mission 2"),
                )
            )
        )
    }
}
