package com.tokopedia.home_component.visitable.shorten

import com.tokopedia.home_component.viewholders.shorten.internal.ShortenVisitable
import com.tokopedia.home_component.viewholders.shorten.factory.ShortenViewFactory

data class MissionWidgetUiModel(
    val data: List<ItemMissionWidgetUiModel>
) : ShortenVisitable {

    override fun type(typeFactory: ShortenViewFactory) = typeFactory.type(this)

    override fun visitableId() = this.hashCode()

    override fun equalsWith(o: Any?) = o == this
}

data class ItemMissionWidgetUiModel(
    val title: String
) : ShortenVisitable by ShortenVisitable.ItemShortenVisitable(
    DealsAndMissionWidgetUiModel.Type.Mission.value
)
