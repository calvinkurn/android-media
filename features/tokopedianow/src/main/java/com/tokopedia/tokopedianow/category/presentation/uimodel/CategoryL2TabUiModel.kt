package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryL2TypeFactory

data class CategoryL2TabUiModel(
    val tabTitleList: List<String>,
    val selectedTabPosition: Int = 0
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
