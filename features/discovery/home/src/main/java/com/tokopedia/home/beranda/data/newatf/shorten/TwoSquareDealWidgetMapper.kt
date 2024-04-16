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

        return DealsWidgetUiModel(
            channelModel = model,
            position = index,
            header = header,
            data = channel.grids.map { grid ->
                val labelGroup = grid.labelGroup.associateBy { it.position }
                val ribbon = labelGroup[Keys.ribbon]

                fun ribbon(): SmallProductModel.Ribbon.Type {
                    return if (ribbon?.type == Default.ribbonRed) {
                        SmallProductModel.Ribbon.Type.Red
                    } else {
                        SmallProductModel.Ribbon.Type.Yellow
                    }
                }

                ItemDealsWidgetUiModel(
                    card = createSmallProductCardModel(grid.labelGroup.toList()).copy(
                        ribbon = SmallProductModel.Ribbon(
                            text = ribbon?.title.orEmpty(),
                            type = ribbon()
                        )
                    ),
                    pageName = labelGroup[Keys.pageName]?.title.orEmpty(),
                    gridId = labelGroup[Keys.gridId]?.title.orEmpty(),
                    url = labelGroup[Keys.url]?.imageUrl.orEmpty(), // this shouldn't be imageUrl
                    appLink = labelGroup[Keys.appLink]?.imageUrl.orEmpty(),
                    campaignCode = labelGroup[Keys.campaignCode]?.title.orEmpty(),
                )
            }
        )
    }
}
