package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokopedianow.annotation.presentation.uimodel.BrandWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowBundleUiModel
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowRepurchaseUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowTickerUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutUiModel

object VisitableMapper {

    fun MutableList<HomeLayoutItemUiModel?>.updateItemById(id: String?, block: () -> HomeLayoutItemUiModel?) {
        getItemIndex(id)?.let { index ->
            block.invoke()?.let { item ->
                removeAt(index)
                add(index, item)
            }
        }
    }

    fun MutableList<HomeLayoutItemUiModel?>.getItemIndex(visitableId: String?): Int? {
        return toMutableList().firstOrNull { it?.layout?.getVisitableId() == visitableId }?.let { indexOf(it) }
    }

    fun Visitable<*>.getVisitableId(): String? {
        return when (this) {
            is HomeLayoutUiModel -> visitableId
            is HomeComponentVisitable -> visitableId()
            is TokoNowRepurchaseUiModel -> id
            is TokoNowCategoryMenuUiModel -> id
            is TokoNowChooseAddressWidgetUiModel -> id
            is TokoNowTickerUiModel -> id
            is TokoNowBundleUiModel -> id
            is BrandWidgetUiModel -> id
            else -> null
        }
    }
}
