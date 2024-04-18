package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.visitable.shorten.MultiTwoSquareWidgetUiModel

object ShortenWidgetMapper {

    fun to2SquareUiModel(data: DynamicHomeChannel, atfData: AtfData): MultiTwoSquareWidgetUiModel {
        val top2ChannelList = data.channels
            .takeIf { it.size <= 2 } ?: return MultiTwoSquareWidgetUiModel()

        // TODO: have been over-comm to BE team to provide the variable for this determination.
        val mission = top2ChannelList.first()
        val deals = top2ChannelList.last()

        return MultiTwoSquareWidgetUiModel(
            id = atfData.atfMetadata.id.toString(),
            showShimmering = atfData.atfMetadata.isShimmer,
            mission = TwoSquareMissionWidgetMapper.map(data, mission),
            thumbnail = TwoSquareThumbnailWidgetMapper.map(data, deals),
            status = MultiTwoSquareWidgetUiModel.Status.Success
        )
    }
}
