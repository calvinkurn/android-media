package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class CategoryNavigationUiModel(
    val id: String = String.EMPTY,
    val title: String = String.EMPTY,
    val categoryListUiModel: List<CategoryNavigationItemUiModel> = emptyList(),
    @TokoNowLayoutState val state: Int = TokoNowLayoutState.LOADING
): Visitable<CategoryTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: CategoryTypeFactory): Int = typeFactory.type(this)
}
