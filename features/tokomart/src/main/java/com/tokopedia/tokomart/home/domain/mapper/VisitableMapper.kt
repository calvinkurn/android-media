package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokomart.home.presentation.uimodel.TokoMartHomeLayoutUiModel

object VisitableMapper {

    fun List<Visitable<*>>.updateByChannelId(channelId: String?, block: () -> Visitable<*>?): List<Visitable<*>> {
        return getItemIndex(channelId)?.let { index ->
            toMutableList().let {
                block.invoke()?.let { item->
                    it[index] = item
                    it
                }
            }
        } ?: this
    }

    private fun Visitable<*>.getChannelId(): String? {
        return when (this) {
            is TokoMartHomeLayoutUiModel -> channelId
            is HomeComponentVisitable -> visitableId()
            else -> null
        }
    }

    private fun List<Visitable<*>>.getItemIndex(channelId: String?): Int? {
        val item = firstOrNull { it.getChannelId() == channelId }
        return item?.let { indexOf(it) }
    }
}