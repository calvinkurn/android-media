package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryL2TypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class CategoryL2HeaderUiModel(
    val id: String,
    val title: String = "",
    val appLink: String = "",
    @TokoNowLayoutState val state: Int = TokoNowLayoutState.LOADING
): Visitable<CategoryL2TypeFactory> {

    override fun type(typeFactory: CategoryL2TypeFactory): Int {
        return typeFactory.type(this)
    }
}
