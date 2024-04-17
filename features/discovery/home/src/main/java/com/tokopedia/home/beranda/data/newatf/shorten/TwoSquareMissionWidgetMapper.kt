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
        val limitTopTwoGrids = channel.grids.take(2)

        return MissionWidgetUiModel(
            channelModel = model,
            position = index,
            header = header,
            data = limitTopTwoGrids.map { grid ->
                val labelGroup = grid.labelGroup.associateBy { it.position }

                ItemMissionWidgetUiModel(
                    id = labelGroup[Keys.ID]?.title.orEmpty(),
                    card = createSmallProductCardModel(grid.labelGroup.toList()),
                    url = labelGroup[Keys.URL]?.imageUrl.orEmpty(), // this shouldn't be imageUrl
                    appLink = labelGroup[Keys.APP_LINK]?.imageUrl.orEmpty(),
                    pageName = labelGroup[Keys.PAGE_NAME]?.title.orEmpty(),
                    categoryId = labelGroup[Keys.CATEGORY_ID]?.title.orEmpty(),
                    productName = labelGroup[Keys.PRODUCT_NAME]?.title.orEmpty(),
                    recommendationType = labelGroup[Keys.RECOMMENDATION_TYPE]?.title.orEmpty(),
                    buType = labelGroup[Keys.BU_TYPE]?.title.orEmpty(),
                    shopId = labelGroup[Keys.SHOP_ID]?.title.orEmpty(),
                    isTopAds = labelGroup[Keys.IS_TOPADS]?.title.orEmpty().toBoolean(),
                    isCarousel = labelGroup[Keys.IS_CAROUSEL]?.title.orEmpty().toBoolean(),
                )
            }
        )
    }
}
