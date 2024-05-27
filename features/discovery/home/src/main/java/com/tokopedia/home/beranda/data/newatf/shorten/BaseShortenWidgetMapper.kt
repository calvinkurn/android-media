package com.tokopedia.home.beranda.data.newatf.shorten

import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper
import com.tokopedia.home.beranda.data.mapper.factory.DynamicChannelComponentMapper.mapToHomeComponentHeader
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.LabelGroup
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenVisitable
import com.tokopedia.home_component.widget.card.SmallProductModel
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.kotlin.extensions.view.isLessThanZero

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
    private var maxToRender = 2

    /**
     * As we want to optimize the ui-model mapper, we introduced a new [ShortenVisitable],
     * where every single shorten widget comes from dynamic-channel bases, we have to map it
     * as UI-ready model to render it into `ContainerMultiTwoSquareViewHolder`.
     */
    abstract fun map(
        data: DynamicHomeChannel,
        channel: DynamicHomeChannel.Channels?,
        verticalPosition: Int
    ): T?

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
    protected fun createSmallProductCardModel(
        grid: DynamicHomeChannel.Grid,
        groups: List<LabelGroup>
    ) = SmallProductModel(
        bannerImageUrl = grid.imageUrl,
        labelGroupList = groups.map {
            SmallProductModel.LabelGroup(
                position = it.position,
                title = it.title,
                type = it.type,
                url = it.imageUrl,
                styles = it.styles.map { style ->
                    SmallProductModel.LabelGroup.Styles(style.key, style.value)
                }
            )
        }
    )

    /**
     * [mapChannelToPartialWidget] simplifies the marshalling process of raw dynamic channel model,
     * hence we can get model-ready such as [ChannelModel], index-position, as well as [ChannelHeader].
     */
    private fun mapChannelToPartialWidget(
        data: DynamicHomeChannel,
        channel: DynamicHomeChannel.Channels
    ): Triple<Int, ChannelModel, ChannelHeader> {
        val widgetPosition = with(data.channels.indexOf(channel)) {
            if (this.isLessThanZero()) 0 else this
        }

        val channelModel = DynamicChannelComponentMapper
            .mapHomeChannelToComponent(channel, widgetPosition)

        return Triple(
            widgetPosition,
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
        const val PAGE_NAME = "home-pagename"
        const val GRID_ID = "home-grid-id"
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
}
