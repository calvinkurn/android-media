package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.visitable.shorten.ItemProductWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ProductWidgetUiModel

object TwoSquareProductWidgetMapper : BaseShortenWidgetMapper<ProductWidgetUiModel>() {

    override fun layout() = "2_square_product"

    override fun map(
        data: DynamicHomeChannel,
        channel: DynamicHomeChannel.Channels?
    ): ProductWidgetUiModel? {
        val widget = widget(data, channel) ?: return null

        return ProductWidgetUiModel(
            channelModel = widget.channelModel,
            position = widget.position,
            header = widget.header,
            data = widget.grids.map { grid ->
                // val labelGroup = grid.labelGroup.associateBy { it.position }

                ItemProductWidgetUiModel(
                    card = createSmallProductCardModel(grid, grid.labelGroup.toList()),
                    tracker = DynamicChannelComponentMapper.mapHomeChannelTrackerToModel(grid)
                )
            }
        )
    }
}
