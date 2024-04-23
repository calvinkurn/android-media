package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper.mapToHomeComponentHeader
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.LabelGroup
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenVisitable
import com.tokopedia.home_component.widget.card.SmallProductModel
import com.tokopedia.home_component_header.model.ChannelHeader

abstract class BaseShortenWidgetMapper<T> {

    /**
     * Differentiate the shorten widget uses channel.layout.
     */
    abstract fun layout(): String

    /**
     * Since the shorten widget needs to maintain compact mode, thus the widget need to clarify
     * how max product item should be got rendered by providing [maxToRender] detail.
     *
     * You could override this variable and define by your own to make shorten widget dynamically.
     * E.g. differentiate between phone mode (2 products) nor tablet mode (4 products).
     */
    protected var maxToRender = 2

    /**
     * As we want to optimize the ui-model mapper, we introduced a new [ShortenVisitable],
     * where every single shorten widget comes from dynamic-channel bases, we have to map it
     * as UI-ready model to render it into `ContainerMultiTwoSquareViewHolder`.
     */
    abstract fun map(data: DynamicHomeChannel, channel: DynamicHomeChannel.Channels?): T?

    fun widget(data: DynamicHomeChannel, channel: DynamicHomeChannel.Channels?): ShortenWidget? {
        if (channel == null) return null

        val (index, model, header) = mapChannelToPartialWidget(data, channel)
        val grids = channel.grids.take(maxToRender)

        return ShortenWidget(
            channelModel = model,
            position = index,
            header = header,
            grids = grids
        )
    }

    /**
     * Each widget that needs to render a small product-card, we can utilize to set the model
     * by using this [createSmallProductCardModel]. As the labelGroup's payload is similar,
     * we can re-use this method nor improving it if we have additional styles occured.
     */
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

    /**
     * [mapChannelToPartialWidget] simplifies the marshalling process of raw dynamic channel model,
     * hence we can get model-ready such as [ChannelModel], index-position, as well as [ChannelHeader].
     */
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

    data class ShortenWidget(
        val channelModel: ChannelModel,
        val position: Int,
        val header: ChannelHeader,
        val grids: List<DynamicHomeChannel.Grid>
    )

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
