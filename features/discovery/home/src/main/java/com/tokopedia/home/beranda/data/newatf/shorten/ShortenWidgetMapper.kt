package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.visitable.shorten.MultiTwoSquareWidgetUiModel

object ShortenWidgetMapper {

    fun to2SquareUiModel(data: DynamicHomeChannel, atfData: AtfData): MultiTwoSquareWidgetUiModel {
        val channelList = data.channels
            .takeIf { it.size <= maxWidgetSideToSideSize() }
            ?.associate { it.layout to it } ?: return MultiTwoSquareWidgetUiModel()

        val mission = channelList[TwoSquareMissionWidgetMapper.layout()]
        val thumbnail = channelList[TwoSquareThumbnailWidgetMapper.layout()]

        return MultiTwoSquareWidgetUiModel(
            id = atfData.atfMetadata.id.toString(),
            showShimmering = atfData.atfMetadata.isShimmer,
            mission = TwoSquareMissionWidgetMapper.map(data, mission),
            thumbnail = TwoSquareThumbnailWidgetMapper.map(data, thumbnail),
            status = MultiTwoSquareWidgetUiModel.Status.Success
        )
    }

    /**
     * Due to channel_horizontal need to show only 4 products with 2 container,
     * we have to set the threshold of amount of dynamic-channel list.
     */
    private fun maxWidgetSideToSideSize() = 2
}
