package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryL2TabAdapterFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class CategoryProductListUiModel(
    val id: String,
    @TokoNowLayoutState val state: Int = TokoNowLayoutState.LOADING
): Visitable<CategoryL2TabAdapterFactory> {

    override fun type(typeFactory: CategoryL2TabAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
