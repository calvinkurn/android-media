package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel

object TwoSquareMissionWidgetMapper : BaseShortenWidgetMapper<MissionWidgetUiModel>() {

    override fun map(
        data: DynamicHomeChannel,
        channel: DynamicHomeChannel.Channels
    ): MissionWidgetUiModel {
        val (index, model, header) = mapChannelToPartialWidget(data, channel)

        return MissionWidgetUiModel(
            channelModel = model,
            position = index,
            header = header,
            data = channel.grids.map { grid ->
                val labelGroup = grid.labelGroup.associateBy { it.position }

                ItemMissionWidgetUiModel(
                    id = labelGroup[Keys.id]?.title.orEmpty(),
                    card = createSmallProductCardModel(grid.labelGroup.toList()),
                    url = labelGroup[Keys.url]?.imageUrl.orEmpty(), // this shouldn't be imageUrl
                    appLink = labelGroup[Keys.appLink]?.imageUrl.orEmpty(),
                    pageName = labelGroup[Keys.pageName]?.title.orEmpty(),
                    categoryId = labelGroup[Keys.categoryId]?.title.orEmpty(),
                    productName = labelGroup[Keys.productName]?.title.orEmpty(),
                    recommendationType = labelGroup[Keys.productName]?.title.orEmpty(),
                    buType = labelGroup[Keys.recommendationType]?.title.orEmpty(),
                    shopId = labelGroup[Keys.buType]?.title.orEmpty(),
                    isTopAds = labelGroup[Keys.shopId]?.title.orEmpty().toBoolean(),
                    isCarousel = labelGroup[Keys.isTopAds]?.title.orEmpty().toBoolean(),
                )
            }
        )
    }
}
