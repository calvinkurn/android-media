package com.tokopedia.tokomart.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokomart.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.TokoMartHomeLayoutUiModel

object VisitableMapper {

    fun List<HomeLayoutItemUiModel>.updateItemById(id: String?, block: () -> HomeLayoutItemUiModel?): List<HomeLayoutItemUiModel> {
        return getItemIndex(id)?.let { index ->
            toMutableList().let {
                block.invoke()?.let { item ->
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

    private fun List<HomeLayoutItemUiModel>.getItemIndex(visitableId: String?): Int? {
        return firstOrNull { it.layout.getVisitableId() == visitableId }?.let { indexOf(it) }
    }
}