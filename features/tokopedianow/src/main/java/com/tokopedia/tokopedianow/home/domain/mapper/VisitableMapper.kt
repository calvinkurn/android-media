package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokopedianow.common.model.TokoNowLayoutUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel

object VisitableMapper {

    fun MutableList<HomeLayoutItemUiModel>.updateItemById(id: String?, block: () -> HomeLayoutItemUiModel?) {
        getItemIndex(id)?.let { index ->
            block.invoke()?.let { item ->
                removeAt(index)
                add(index, item)
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.getItemIndex(visitableId: String?): Int? {
        return firstOrNull { it.layout.getVisitableId() == visitableId }?.let { indexOf(it) }
    }

    private fun Visitable<*>.getVisitableId(): String? {
        return when (this) {
            is HomeLayoutUiModel -> visitableId
            is HomeComponentVisitable -> visitableId()
            is TokoNowLayoutUiModel -> visitableId
            else -> null
        }
    }
}