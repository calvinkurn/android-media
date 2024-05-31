package com.tokopedia.home_component.visitable.shorten

import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelTracker
import com.tokopedia.home_component.viewholders.shorten.factory.ShortenViewFactory
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenVisitable
import com.tokopedia.home_component.visitable.shorten.MultiTwoSquareWidgetUiModel.Type as ItemTwoSquareType
import com.tokopedia.home_component.widget.card.SmallProductModel
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.kotlin.model.ImpressHolder

data class ProductWidgetUiModel(
    val channelModel: ChannelModel,
    val position: Int,
    val header: ChannelHeader = ChannelHeader(),
    val data: List<ItemProductWidgetUiModel>
) : ShortenVisitable {

    override fun type(typeFactory: ShortenViewFactory) = typeFactory.type(this)
    override fun visitableId() = channelModel.id.toIntOrNull() ?: this.hashCode()
    override fun equalsWith(o: Any?) = o == this

    override fun itemCount() = data.size
}

data class ItemProductWidgetUiModel(
    val card: SmallProductModel,
    val appLink: String,
    val tracker: ChannelTracker = ChannelTracker(),
    val verticalPosition: Int
) : ShortenVisitable by ShortenVisitable.ItemShortenVisitable(ItemTwoSquareType.Product.value) {

    val impression = ImpressHolder()
}
