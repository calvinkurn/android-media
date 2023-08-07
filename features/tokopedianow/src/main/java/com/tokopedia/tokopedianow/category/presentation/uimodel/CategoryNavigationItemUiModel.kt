package com.tokopedia.tokopedianow.category.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryNavigationItemTypeFactory

data class CategoryNavigationItemUiModel(
    val id: String = String.EMPTY,
    val title: String = String.EMPTY,
    val imageUrl: String? = null,
    val appLink: String = String.EMPTY,
    val warehouseId: String = String.EMPTY,
    val headerName: String = String.EMPTY,
    val backgroundLightColor: String = String.EMPTY,
    val backgroundDarkColor: String = String.EMPTY
): Visitable<CategoryNavigationItemTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: CategoryNavigationItemTypeFactory): Int = typeFactory.type(this)
}
