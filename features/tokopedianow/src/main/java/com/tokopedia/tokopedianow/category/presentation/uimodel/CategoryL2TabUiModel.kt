package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabData
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryL2TypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class CategoryL2TabUiModel(
    val id: String = "",
    val titleList: List<String> = emptyList(),
    val tabList: List<CategoryL2TabData> = emptyList(),
    var selectedTabPosition: Int = 0,
    @TokoNowLayoutState val state: Int = TokoNowLayoutState.LOADING
) : Visitable<CategoryL2TypeFactory> {

    override fun type(typeFactory: CategoryL2TypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getChangePayload(newItem: CategoryL2TabUiModel): Boolean {
        return when {
            selectedTabPosition != newItem.selectedTabPosition -> true
            else -> false
        }
    }
}
