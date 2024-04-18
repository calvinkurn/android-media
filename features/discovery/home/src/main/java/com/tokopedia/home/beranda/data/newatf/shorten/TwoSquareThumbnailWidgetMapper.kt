package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.visitable.shorten.ThumbnailWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemThumbnailWidgetUiModel
import com.tokopedia.home_component.widget.card.SmallProductModel

object TwoSquareThumbnailWidgetMapper : BaseShortenWidgetMapper<ThumbnailWidgetUiModel>() {

    override fun map(
        data: DynamicHomeChannel,
        channel: DynamicHomeChannel.Channels
    ): ThumbnailWidgetUiModel {
        val (index, model, header) = mapChannelToPartialWidget(data, channel)
        val limitTopTwoGrids = channel.grids.take(2)

        return ThumbnailWidgetUiModel(
            channelModel = model,
            position = index,
            header = header,
            data = limitTopTwoGrids.map { grid ->
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
                    card = createSmallProductCardModel(grid.labelGroup.toList()).copy(
                        ribbon = SmallProductModel.Ribbon(
                            text = ribbon?.title.orEmpty(),
                            type = ribbon()
                        )
                    ),
                    pageName = labelGroup[Keys.PAGE_NAME]?.title.orEmpty(),
                    gridId = labelGroup[Keys.GRID_ID]?.title.orEmpty(),
                    url = labelGroup[Keys.URL]?.imageUrl.orEmpty(), // this shouldn't be imageUrl
                    appLink = labelGroup[Keys.APP_LINK]?.imageUrl.orEmpty(),
                    campaignCode = labelGroup[Keys.CAMPAIGN_CODE]?.title.orEmpty(),
                )
            }
        )
    }
}
