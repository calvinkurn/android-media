package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.visitable.shorten.DealsWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemDealsWidgetUiModel
import com.tokopedia.home_component.widget.card.SmallProductModel

object TwoSquareDealWidgetMapper : BaseShortenWidgetMapper<DealsWidgetUiModel>() {

    override fun map(
        data: DynamicHomeChannel,
        channel: DynamicHomeChannel.Channels
    ): DealsWidgetUiModel {
        val (index, model, header) = mapChannelToPartialWidget(data, channel)
        val limitTopTwoGrids = channel.grids.take(2)

        return DealsWidgetUiModel(
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

                ItemDealsWidgetUiModel(
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
