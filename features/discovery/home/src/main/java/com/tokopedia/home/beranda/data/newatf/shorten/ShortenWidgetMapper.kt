package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper.mapToHomeComponentHeader
import com.tokopedia.home.beranda.data.newatf.AtfData
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.shorten.DealsAndMissionWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.DealsWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemDealsWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ItemMissionWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel
import com.tokopedia.home_component.widget.card.SmallProductModel
import com.tokopedia.home_component_header.model.ChannelHeader

object ShortenWidgetMapper {

    fun to2SquareUiModel(data: DynamicHomeChannel, atfData: AtfData): DealsAndMissionWidgetUiModel {
        val mission = data.channels.first()
        val deals = data.channels.last()

        return DealsAndMissionWidgetUiModel(
            id = atfData.atfMetadata.id.toString(),
            showShimmering = atfData.atfMetadata.isShimmer,
            deals = mapTo2SquareDealsWidget(data, deals),
            mission = mapTo2SquareMissionWidget(data, mission)
        )
    }

    private fun mapTo2SquareDealsWidget(
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

                val title = labelGroup["home-title"]
                val titleStyle = title?.styles.orEmpty().associate { it.key to it.value }

                val subtitle = labelGroup["home-subtitle"]
                val subtitleStyle = subtitle?.styles.orEmpty().associate { it.key to it.value }

                val ribbon = labelGroup["ri_ribbon"]

                ItemDealsWidgetUiModel(
                    card = SmallProductModel(
                        bannerImageUrl = labelGroup["home-image-url"]?.title.orEmpty(),
                        ribbon = SmallProductModel.Ribbon(
                            text = ribbon?.title.orEmpty(),
                            type = if (ribbon?.type == "red") {
                                SmallProductModel.Ribbon.Type.Red
                            } else {
                                SmallProductModel.Ribbon.Type.Yellow
                            }
                        ),
                        title = Pair(
                            title?.title.orEmpty(),
                            SmallProductModel.TextStyle(
                                isBold = titleStyle["text-format"] == "bold",
                                textColor = titleStyle["text-color"].orEmpty(),
                                shouldRenderHtmlFormat = titleStyle["text-format"] == null
                            )
                        ),
                        subtitle = Pair(
                            subtitle?.title.orEmpty(),
                            SmallProductModel.TextStyle(
                                isBold = subtitleStyle["text-format"] == "bold",
                                textColor = subtitleStyle["text-color"].orEmpty(),
                                shouldRenderHtmlFormat = subtitleStyle["text-format"] == null
                            )
                        )
                    ),
                    pageName = labelGroup["home-pagename"]?.title.orEmpty(),
                    gridId = labelGroup["home-grid-id"]?.title.orEmpty(),
                    url = labelGroup["home-url"]?.imageUrl.orEmpty(), // this shouldn't be imageUrl
                    appLink = labelGroup["home-applink"]?.imageUrl.orEmpty(),
                    campaignCode = labelGroup["home-campaign-code"]?.title.orEmpty(),
                )
            }
        )
    }

    private fun mapTo2SquareMissionWidget(
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

                val title = labelGroup["home-title"]
                val titleStyle = title?.styles.orEmpty().associate { it.key to it.value }

                val subtitle = labelGroup["home-subtitle"]
                val subtitleStyle = subtitle?.styles.orEmpty().associate { it.key to it.value }

                ItemMissionWidgetUiModel(
                    id = labelGroup["home-id"]?.title.orEmpty(),
                    card = SmallProductModel(
                        bannerImageUrl = labelGroup["home-image-url"]?.title.orEmpty(),
                        title = Pair(
                            title?.title.orEmpty(),
                            SmallProductModel.TextStyle(
                                isBold = titleStyle["text-format"] == "bold",
                                textColor = titleStyle["text-color"].orEmpty(),
                                shouldRenderHtmlFormat = titleStyle["text-format"] == null
                            )
                        ),
                        subtitle = Pair(
                            subtitle?.title.orEmpty(),
                            SmallProductModel.TextStyle(
                                isBold = subtitleStyle["text-format"] == "bold",
                                textColor = subtitleStyle["text-color"].orEmpty(),
                                shouldRenderHtmlFormat = subtitleStyle["text-format"] == null
                            )
                        )
                    ),
                    url = labelGroup["home-url"]?.imageUrl.orEmpty(), // this shouldn't be imageUrl
                    appLink = labelGroup["home-applink"]?.imageUrl.orEmpty(),
                    pageName = labelGroup["home-pagename"]?.title.orEmpty(),
                    categoryId = labelGroup["home-category-id"]?.title.orEmpty(),
                    productName = labelGroup["home-product-name"]?.title.orEmpty(),
                    recommendationType = labelGroup["home-recommendation-type"]?.title.orEmpty(),
                    buType = labelGroup["home-bu-type"]?.title.orEmpty(),
                    shopId = labelGroup["home-shop-id"]?.title.orEmpty(),
                    isTopAds = labelGroup["home-is-topads"]?.title.orEmpty().toBoolean(),
                    isCarousel = labelGroup["home-is-carousel"]?.title.orEmpty().toBoolean(),
                )
            }
        )
    }

    private fun mapChannelToPartialWidget(
        data: DynamicHomeChannel,
        channel: DynamicHomeChannel.Channels
    ): Triple<Int, ChannelModel, ChannelHeader> {
        val indexPosition = data.channels.indexOf(channel)
        val channelModel = DynamicChannelComponentMapper
            .mapHomeChannelToComponent(channel, indexPosition)

        return Triple(
            indexPosition,
            channelModel,
            channel.header.mapToHomeComponentHeader(),
        )
    }
}
