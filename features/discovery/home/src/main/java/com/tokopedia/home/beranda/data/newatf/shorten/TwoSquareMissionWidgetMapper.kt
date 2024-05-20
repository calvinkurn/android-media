package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel

object TwoSquareMissionWidgetMapper : BaseShortenWidgetMapper<MissionWidgetUiModel>() {

    override fun layout() = "2_square_mission"

    override fun map(
        data: DynamicHomeChannel,
        channel: DynamicHomeChannel.Channels?,
        verticalPosition: Int,
    ): MissionWidgetUiModel? {
        val widget = widget(data, channel) ?: return null

        return MissionWidgetUiModel(
            channelModel = widget.channelModel,
            position = widget.position,
            header = widget.header,
            data = widget.grids.mapIndexed { index, grid ->
                val labelGroup = grid.labelGroup.associate { it.position to it.title }

                ItemMissionWidgetUiModel(
                    id = labelGroup[Keys.ID].orEmpty(),
                    tracker = DynamicChannelComponentMapper.mapHomeChannelTrackerToModel(channel, grid),
                    card = createSmallProductCardModel(grid, grid.labelGroup.toList()),
                    verticalPosition = verticalPosition,
                    cardPosition = index,
                    url = grid.url,
                    appLink = grid.applink,
                    pageName = labelGroup[Keys.PAGE_NAME].orEmpty(),
                    categoryId = labelGroup[Keys.CATEGORY_ID].orEmpty(),
                    productName = labelGroup[Keys.PRODUCT_NAME].orEmpty(),
                    recommendationType = labelGroup[Keys.RECOMMENDATION_TYPE].orEmpty(),
                    buType = labelGroup[Keys.BU_TYPE].orEmpty(),
                    shopId = labelGroup[Keys.SHOP_ID].orEmpty(),
                    isTopAds = labelGroup[Keys.IS_TOPADS].orEmpty().toBoolean(),
                    isCarousel = labelGroup[Keys.IS_CAROUSEL].orEmpty().toBoolean(),
                )
            }
        )
    }
}
