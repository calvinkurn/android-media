package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.visitable.shorten.ThumbnailWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemThumbnailWidgetUiModel
import com.tokopedia.home_component.widget.card.SmallProductModel

object TwoSquareThumbnailWidgetMapper : BaseShortenWidgetMapper<ThumbnailWidgetUiModel>() {

    override fun layout() = "2_square_thumbnail"

    override fun map(
        data: DynamicHomeChannel,
        channel: DynamicHomeChannel.Channels?
    ): ThumbnailWidgetUiModel? {
        val widget = widget(data, channel) ?: return null

        return ThumbnailWidgetUiModel(
            channelModel = widget.channelModel,
            position = widget.position,
            header = widget.header,
            data = widget.grids.map { grid ->
                val labelGroup = grid.labelGroup.associateBy { it.position }
                val ribbon = labelGroup[Keys.RIBBON]

                fun ribbon(): SmallProductModel.Ribbon.Type {
                    return if (ribbon?.type == Default.RIBBON_RED) {
                        SmallProductModel.Ribbon.Type.Red
                    } else {
                        SmallProductModel.Ribbon.Type.Gold
                    }
                }

                ItemThumbnailWidgetUiModel(
                    tracker = DynamicChannelComponentMapper.mapHomeChannelTrackerToModel(grid),
                    card = createSmallProductCardModel(grid, grid.labelGroup.toList()).copy(
                        ribbon = SmallProductModel.Ribbon(
                            text = ribbon?.title.orEmpty(),
                            type = ribbon()
                        )
                    ),
                    pageName = labelGroup[Keys.PAGE_NAME]?.title.orEmpty(),
                    gridId = labelGroup[Keys.GRID_ID]?.title.orEmpty(),
                    url = grid.url,
                    appLink = grid.applink,
                    campaignCode = labelGroup[Keys.CAMPAIGN_CODE]?.title.orEmpty(),
                )
            }
        )
    }
}
