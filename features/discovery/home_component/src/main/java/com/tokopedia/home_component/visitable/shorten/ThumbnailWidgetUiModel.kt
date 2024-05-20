package com.tokopedia.home_component.visitable.shorten

import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelTracker
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenVisitable
import com.tokopedia.home_component.viewholders.shorten.internal.ShortenVisitable.ItemShortenVisitable
import com.tokopedia.home_component.viewholders.shorten.factory.ShortenViewFactory
import com.tokopedia.home_component.visitable.shorten.MultiTwoSquareWidgetUiModel.Type as ItemTwoSquareType
import com.tokopedia.home_component.widget.card.SmallProductModel
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.kotlin.model.ImpressHolder

data class ThumbnailWidgetUiModel(
    val channelModel: ChannelModel,
    val position: Int,
    val header: ChannelHeader = ChannelHeader(),
    val data: List<ItemThumbnailWidgetUiModel>
) : ShortenVisitable {

    override fun type(typeFactory: ShortenViewFactory) = typeFactory.type(this)
    override fun visitableId() = this.hashCode()
    override fun equalsWith(o: Any?) = o == this

    override fun itemCount() = data.size
}

data class ItemThumbnailWidgetUiModel(
    val card: SmallProductModel,
    val verticalPosition: Int,
    val cardPosition: Int,
    val tracker: ChannelTracker = ChannelTracker(),
    val pageName: String,
    val gridId: String,
    val url: String,
    val appLink: String,
    val campaignCode: String
) : ShortenVisitable by ItemShortenVisitable(ItemTwoSquareType.Thumbnail.value) {

    val impression = ImpressHolder()
}
