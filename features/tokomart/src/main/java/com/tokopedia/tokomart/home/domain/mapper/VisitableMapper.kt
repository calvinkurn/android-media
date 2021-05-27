package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokomart.home.presentation.uimodel.TokoMartHomeLayoutUiModel

object VisitableMapper {

    fun List<Visitable<*>>.updateItemById(id: String?, block: () -> Visitable<*>?): List<Visitable<*>> {
        return getItemIndex(id)?.let { index ->
            toMutableList().let {
                block.invoke()?.let { item->
                    it[index] = item
                    it
                }
            }
        } ?: this
    }

    private fun Visitable<*>.getVisitableId(): String? {
        return when (this) {
            is TokoMartHomeLayoutUiModel -> visitableId
            is HomeComponentVisitable -> visitableId()
            else -> null
        }
    }

    private fun List<Visitable<*>>.getItemIndex(visitableId: String?): Int? {
        return firstOrNull { it.getVisitableId() == visitableId }?.let { indexOf(it) }
    }
}