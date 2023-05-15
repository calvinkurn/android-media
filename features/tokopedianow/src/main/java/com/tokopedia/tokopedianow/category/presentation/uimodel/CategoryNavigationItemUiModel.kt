package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryNavigationItemTypeFactory

data class CategoryNavigationItemUiModel(
    val id: String = "",
    val title: String = "",
    val imageUrl: String? = null,
    val appLink: String = "",
    val warehouseId: String = "",
    val headerName: String = "",
    val color: String = ""
): Visitable<CategoryNavigationItemTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: CategoryNavigationItemTypeFactory): Int = typeFactory.type(this)
}
