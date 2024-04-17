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

        val title = labelGroup[Keys.TITLE]
        val titleStyle = title?.styles.orEmpty().associate { it.key to it.value }

        val subtitle = labelGroup[Keys.SUBTITLE]
        val subtitleStyle = subtitle?.styles.orEmpty().associate { it.key to it.value }

        return SmallProductModel(
            bannerImageUrl = labelGroup[Keys.IMAGE_URL]?.title.orEmpty(),
            title = Pair(
                title?.title.orEmpty(),
                SmallProductModel.TextStyle(
                    isBold = titleStyle[Keys.TEXT_FORMAT] == Default.TEXT_BOLD,
                    textColor = titleStyle[Keys.TEXT_COLOR].orEmpty(),
                    shouldRenderHtmlFormat = titleStyle[Keys.TEXT_FORMAT] == null
                )
            ),
            subtitle = Pair(
                subtitle?.title.orEmpty(),
                SmallProductModel.TextStyle(
                    isBold = subtitleStyle[Keys.TEXT_FORMAT] == Default.TEXT_BOLD,
                    textColor = subtitleStyle[Keys.TEXT_COLOR].orEmpty(),
                    shouldRenderHtmlFormat = subtitleStyle[Keys.TEXT_FORMAT] == null
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
        const val TITLE = "home-title"
        const val SUBTITLE = "home-subtitle"
        const val RIBBON = "ri_ribbon"
        const val IMAGE_URL = "home-image-url"
        const val TEXT_FORMAT = "text-format"
        const val TEXT_COLOR = "text-color"
        const val PAGE_NAME = "home-pagename"
        const val GRID_ID = "home-grid-id"
        const val URL = "home-url"
        const val APP_LINK = "home-applink"
        const val CAMPAIGN_CODE = "home-campaign-code"
        const val ID = "home-id"
        const val CATEGORY_ID = "ome-category-id"
        const val PRODUCT_NAME = "home-product-name"
        const val RECOMMENDATION_TYPE = "home-recommendation-type"
        const val BU_TYPE = "home-bu-type"
        const val SHOP_ID = "home-shop-id"
        const val IS_TOPADS = "home-is-topads"
        const val IS_CAROUSEL = "home-is-carousel"
    }

    protected object Default {
        const val TEXT_BOLD = "bold"
        const val RIBBON_RED = "red"
    }
}
