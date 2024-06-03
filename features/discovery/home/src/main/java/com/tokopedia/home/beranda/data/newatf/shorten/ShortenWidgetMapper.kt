package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.visitable.shorten.MultiTwoSquareWidgetUiModel

object ShortenWidgetMapper {

    fun to2SquareUiModel(data: DynamicHomeChannel, atfData: AtfData, verticalPosition: Int): MultiTwoSquareWidgetUiModel {
        if (data.channels.isEmpty()) return MultiTwoSquareWidgetUiModel()

        val channel = data.channels
            .take(maxWidgetSideToSideSize())
            .associateBy { it.layout }

        val mission = channel[TwoSquareMissionWidgetMapper.layout()]
        val thumbnail = channel[TwoSquareThumbnailWidgetMapper.layout()]
        val product = channel[TwoSquareProductWidgetMapper.layout()]

        return MultiTwoSquareWidgetUiModel(
            id = atfData.atfMetadata.id.toString(),
            showShimmering = atfData.atfMetadata.isShimmer,
            backgroundGradientColor = ArrayList(atfData.style.gradientColor),
            mission = TwoSquareMissionWidgetMapper.map(data, mission, verticalPosition),
            thumbnail = TwoSquareThumbnailWidgetMapper.map(data, thumbnail, verticalPosition),
            product = TwoSquareProductWidgetMapper.map(data, product, verticalPosition),
            status = MultiTwoSquareWidgetUiModel.Status.Success
        )
    }

    /**
     * Due to channel_horizontal need to show only 4 products with 2 container,
     * we have to set the threshold of amount of dynamic-channel list.
     */
    private fun maxWidgetSideToSideSize() = 2
}
