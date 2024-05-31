package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.visitable.shorten.ThumbnailWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemThumbnailWidgetUiModel

object TwoSquareThumbnailWidgetMapper : BaseShortenWidgetMapper<ThumbnailWidgetUiModel>() {

    override fun layout() = "2_square_thumbnail"

    override fun map(
        data: DynamicHomeChannel,
        channel: DynamicHomeChannel.Channels?,
        verticalPosition: Int
    ): ThumbnailWidgetUiModel? {
        val widget = widget(data, channel) ?: return null

        return ThumbnailWidgetUiModel(
            channelModel = widget.channelModel,
            position = widget.position,
            header = widget.header,
            data = widget.grids.mapIndexed { index, grid ->
                val labelGroup = grid.labelGroup.associate { it.position to it.title }

                ItemThumbnailWidgetUiModel(
                    tracker = DynamicChannelComponentMapper.mapHomeChannelTrackerToModel(channel, grid),
                    verticalPosition = verticalPosition,
                    cardPosition = index,
                    card = createSmallProductCardModel(grid, grid.labelGroup.toList()),
                    pageName = labelGroup[Keys.PAGE_NAME].orEmpty(),
                    gridId = labelGroup[Keys.GRID_ID].orEmpty(),
                    url = grid.url,
                    appLink = grid.applink,
                    campaignCode = labelGroup[Keys.CAMPAIGN_CODE].orEmpty(),
                )
            }
        )
    }
}
