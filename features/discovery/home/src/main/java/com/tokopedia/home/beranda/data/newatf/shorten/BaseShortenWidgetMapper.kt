package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper.mapToHomeComponentHeader
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.LabelGroup
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.widget.card.SmallProductModel
import com.tokopedia.home_component_header.model.ChannelHeader

abstract class BaseShortenWidgetMapper<T> {

    abstract fun map(data: DynamicHomeChannel, channel: DynamicHomeChannel.Channels): T

    protected fun createSmallProductCardModel(groups: List<LabelGroup>): SmallProductModel {
        val labelGroup = groups.associateBy { it.position }

        val title = labelGroup[Keys.title]
        val titleStyle = title?.styles.orEmpty().associate { it.key to it.value }

        val subtitle = labelGroup[Keys.subtitle]
        val subtitleStyle = subtitle?.styles.orEmpty().associate { it.key to it.value }

        return SmallProductModel(
            bannerImageUrl = labelGroup[Keys.imageUrl]?.title.orEmpty(),
            title = Pair(
                title?.title.orEmpty(),
                SmallProductModel.TextStyle(
                    isBold = titleStyle[Keys.textFormat] == Default.textBold,
                    textColor = titleStyle[Keys.textColor].orEmpty(),
                    shouldRenderHtmlFormat = titleStyle[Keys.textFormat] == null
                )
            ),
            subtitle = Pair(
                subtitle?.title.orEmpty(),
                SmallProductModel.TextStyle(
                    isBold = subtitleStyle[Keys.textFormat] == Default.textBold,
                    textColor = subtitleStyle[Keys.textColor].orEmpty(),
                    shouldRenderHtmlFormat = subtitleStyle[Keys.textFormat] == null
                )
            )
        )
    }

    protected fun mapChannelToPartialWidget(
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

    protected object Keys {
        val title = "home-title"
        val subtitle = "home-subtitle"
        val ribbon = "ri_ribbon"
        val imageUrl = "home-image-url"
        val textFormat = "text-format"
        val textColor = "text-color"
        val pageName = "home-pagename"
        val gridId = "home-grid-id"
        val url = "home-url"
        val appLink = "home-applink"
        val campaignCode = "home-campaign-code"
        val id = "home-id"
        val categoryId = "ome-category-id"
        val productName = "home-product-name"
        val recommendationType = "home-recommendation-type"
        val buType = "home-bu-type"
        val shopId = "home-shop-id"
        val isTopAds = "home-is-topads"
        val isCarousel = "home-is-carousel"
    }

    protected object Default {
        val textBold = "bold"
        val ribbonRed = "red"
    }
}
