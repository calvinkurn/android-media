package com.tokopedia.home_component.visitable.shorten

import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenVisitable
import com.tokopedia.home_component.viewholders.shorten.factory.ShortenViewFactory
import com.tokopedia.home_component.widget.card.SmallProductModel
import com.tokopedia.home_component_header.model.ChannelHeader

data class DealsWidgetUiModel(
    val channelModel: ChannelModel,
    val header: ChannelHeader = ChannelHeader(),
    val data: List<ItemDealsWidgetUiModel>
) : ShortenVisitable {

    override fun type(typeFactory: ShortenViewFactory) = typeFactory.type(this)
    override fun visitableId() = this.hashCode()
    override fun equalsWith(o: Any?) = o == this
}

data class ItemDealsWidgetUiModel(
    val card: SmallProductModel,
    val pageName: String,
    val gridId: String,
    val url: String,
    val appLink: String,
    val campaignCode: String
) : ShortenVisitable by ShortenVisitable.ItemShortenVisitable(
    DealsAndMissionWidgetUiModel.Type.Deals.value
)
